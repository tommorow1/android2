package com.yourockdude.ruinnet;

import android.app.Application;
import android.content.res.Configuration;

import com.vk.sdk.VKSdk;

/**
 * Created by yourockinc on 19.10.17.
 */

public class MyApplication extends Application {
    private static MyApplication singleton;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize( this);
        singleton = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static MyApplication getInstance(){
        return singleton;
    }

}
