#Camera Libs
##在XML文件中使用
```java
<com.babypat.common.camera.CameraSurfaceView
        android:id="@+id/surfaceView"
        android:layout_width="match_parent"
        android:layout_height="100dp"/>
```
##设置CameraSurfaceview相关参数
```java
CameraSurfaceView surfaceView;
ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
layoutParams.width = DisplayUtils.getSceenWidth();
layoutParams.height = (int) (DisplayUtils.getSceenWidth() * 4.0f / 3f);
surfaceView.setLayoutParams(layoutParams);
//设置定点对焦
surfaceView.setPointFocus(true, focusIndex);
//设置SurfaceView回调监听，回调将会在camera打开,SurfceView初始化完成后回调
surfaceView.
setOnCameraSurfaceListener(new CameraSurfaceView.OnCameraSurfaceListener() {
            @Override
            public void afterCreate() {
                changeDisplayUISize();
            }
});

```
## 同步生命周期
```java
	//同步生命周期
    @Override
    protected void onResume() {
        super.onResume();
        CameraNative.newInst(self, surfaceView);
        mCameraNative.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraNative.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraNative.onDestory();
    }

```

##获取CameraNative实例并设置监听?
```java
//get the singleton of CameraNative
private CameraNative mCameraNative;
mCameraNative = CameraNative.getInst();

//设置保存图片的路径
mCameraNative.setSaveDir(new File(ImageUtils.getSystemDcimPath()));
//设置闪光灯监听，有三个回调可以自由实现
mCameraNative.setOnFlashChangeListener(new CameraNative.OnFlashChangeListener() {
            @Override
            public boolean OnTurnFlashOn() {
            //your code
                   return true;
            }
});

//设置照片拍摄的监听的监听，将会实现三个方法
mCameraNative.setOnSavePicListener(this);
@Override
public void InSaveProgress(int num, float percent) {
        L.info("正在存储 " + num + "  rate  " + percent);
        //由于拍摄和存储是异步进行的，在这里可以回调保存的进度
}
@Override
public void OnSaveOver() {
        L.info("保存完成");
        //保存完成，回调发生
}

@Override
public void CanotTake() {
        ToastUtil.show("稍候操作");
        //camera拍摄过快时将会产生异常，会回调改方法
}

//错误回调监听
mCameraNative.setOnErrorListener(new CameraNative.OnErrorListener() {
            @Override
            public void error(String errMsg) {
            //错误信息，默认错误信息仍旧是会打印的
                
            }
        });
```

##闪光灯API
```java
//切换闪光灯
//如果相机支持将会切换到自动状态，否则直接切换到关闭状态，是可以自适应的切换模式，需要设置按钮和相关资源
//flashBtn ImageView 切换按钮 可以为空，为空时不切换
//res int[] 资源数组，长度必须是3，可以为空，为空时不切换
public void toogleLightWithAuto(ImageView flashBtn, int... res)
mCameraNative.toogleLightWithAuto(flashBtn,
                R.mipmap.camera_flashon,
                 R.mipmap.camera_flashauto,
                  R.mipmap.camera_flashoff);
                  
//开启和关闭状态切换
//flashBtn ImageView 切换按钮 可以为空，为空时不切换
//res int[] 资源数组，长度必须是2，可以为空，为空时不切换
public void toogleLight(ImageView flashBtn, int... res) 
mCameraNative.toogleLight(flashBtn,
					R.mipmap.camera_flashon, 
						R.mipmap.camera_flashoff);
```
##照片大小API
```java
//自动切换图片资源同时切换图片大小
//size参数 为CameraNative.NotConvert时将会自己切换否则切换到制定状态(CameraNative.NotConvert,CameraNative.One2One,CameraNative.Four2Three)
public void switchPicSize(int size, ImageView iv, int... res) 
//无关资源切换参数同上
public void switchPicSize(int size)
public boolean isSizeOne2One() 
public boolean isFour2Three()
```

##照片模式API
```java
//切换照片模式，两种模式
//MODE_PIC 照片模式，像素在300以上 MODE_GIF 连拍模式，相片像素640*480

//切换拍照模式,同时重新初始化相机
public void switchTakeMode(int mode)
public boolean isTakePic()
public boolean isTakeGif()
```

##拍照API
```java
//拍摄照片
//fileName 指定文件名存储,不能为空
//接口
public static abstract class OnTakePicListener {
//拍摄获得的二进制数组
        public void onTakePic(byte[] data) {

        }
//处理之后的bitmap
        public void onTakePic(Bitmap bit) {

        }
//是否处理成bitmap,默认不处理
        public boolean isConvert2Bitmap() {
            return false;
        }
//压缩的sampleSize,默认不压缩
        public int getInSampleSize(byte[] data) {
            return 1;
        }
}
public boolean doTakePic(String fileName, boolean isConvert2Bit, OnTakePicListener listener)


//快速连拍
//开始连拍时调用doStartTakeFastPic()
public void doStartTakeFastPic()
//拍摄一张，fileName == null时不存储，使用listener获取拍摄的数据
public void doTakeFastPic(String fileName, OnTakePicListener listener)
//拍摄完毕停止连拍
public void doStopFastPic() 
```


##图片处理API
```java
//在外部处理byte数组,返回bitmap
public Bitmap handlePicData(boolean isFast, byte[] data, int sampleSize)
```


##其他
```java
//由于存储照片是异步的，拍摄完毕之后需要调用doTakePicOver()方法，并在OnSaveOver()回调中执行相关操作
doTakePicOver()
//停止照片自定旋转,横屏拍摄的照片将不会自动横向显示
public void shutDownAutoRotate() 
```



