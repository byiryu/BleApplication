package com.byiryu.bleapplication.observer

import com.byiryu.bleapplication.data.BleData
import com.clj.fastble.data.BleDevice
import java.util.ArrayList

object ObserverManager : Observable {

    private val observers = ArrayList<Observer>()

    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun deleteObserver(observer: Observer) {
        var i = observers.indexOf(observer)
        if (i>= 0)
            observers.remove(observer)
    }

    override fun connectFail(bleDevice: BleDevice) {
       for(i in observers.indices){
           var o  = observers.get(i)
           o.connectFail(bleDevice)
       }

    }

    override fun connected(bleDevice: BleDevice) {
        for(i in observers.indices){
            var o = observers.get(i)
            o.connected(bleDevice)
        }
    }

    override fun disConnected(bleDevice: BleDevice) {
        for(i in observers.indices){
            var o = observers.get(i)
            o.disConnected(bleDevice)
        }
    }

    override fun notifyData(bleData: BleData) {
        for(i in observers.indices){
            var o = observers.get(i)
            o.receiveNotify(bleData)
        }
    }
}