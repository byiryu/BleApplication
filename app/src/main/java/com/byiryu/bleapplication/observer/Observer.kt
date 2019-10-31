package com.byiryu.bleapplication.observer

import com.byiryu.bleapplication.data.BleData
import com.clj.fastble.data.BleDevice

interface Observer{

    fun connectFail(bleDevice: BleDevice)

    fun connected(bleDevice: BleDevice)

    fun disConnected(bleDevice: BleDevice)

    fun receiveNotify(bleData: BleData)

}