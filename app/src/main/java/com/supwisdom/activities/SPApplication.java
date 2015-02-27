package com.supwisdom.activities;

import android.app.Application;

/**
 * Created with IntelliJ IDEA.
 * User: tangcheng
 * Date: 13-4-23
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class SPApplication extends Application {
    private static SPApplication sInstance = null;

    public static SPApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
