package com.example.bloold.buildp

import android.support.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary
import com.vk.sdk.VKSdk

/**
 * Created by yourockinc on 19.10.17.
 */

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        instance = this

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        lateinit var instance: MyApplication
    }

}
