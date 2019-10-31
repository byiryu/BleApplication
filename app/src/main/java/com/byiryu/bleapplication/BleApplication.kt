package com.byiryu.bleapplication

import android.app.Application
import com.clj.fastble.BleManager

class BleApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        BleManager.getInstance().init(this)
    }
}