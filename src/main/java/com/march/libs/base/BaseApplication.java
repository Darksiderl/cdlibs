package com.march.libs.base;

import android.app.Application;

/**
 * CdLibsTest     com.march.libs.base
 * Created by 陈栋 on 16/1/30.
 * 功能:
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstApp = this;
    }

    public static BaseApplication getInst() {
        return mInstApp;
    }

}
