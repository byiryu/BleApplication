package com.byiryu.bleapplication.util

import android.bluetooth.BluetoothGatt
import android.util.Log
import com.byiryu.bleapplication.data.BleData
import com.byiryu.bleapplication.observer.ObserverManager
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import java.lang.StringBuilder
import java.math.BigInteger


object BluetoothUtils{
    val SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"
    val CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
    var mBleDevice : BleDevice? = null
    private var HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    fun bytesToHex(bytes: ByteArray): String{

        var hexChar = CharArray(bytes.size*2)

        for(i in bytes.indices){
            val v = bytes[i].toInt() and 0xFF
            hexChar[i*2] = HEX_ARRAY[v.ushr(4)]
            hexChar[i * 2  +1] = HEX_ARRAY[v and 0x0F]
        }

        return String(hexChar)
    }

    fun hexToString(hexStr : String) :String {

        var output = StringBuilder("...")
        var index = 0
        while(index < hexStr.length) {
            var str = hexStr.substring(index, index+2)
            output.append(Integer.parseInt(str, 16))
            index+=2
        }

        return output.toString()
    }

    fun stringToHex(string : String): String{
        var output = StringBuilder("...")

        for(c in string.toCharArray()){
            var i = c.toInt()
            output.append(String.format("%02X",i).toUpperCase())
        }

        return output.toString()

    }

    fun hexToByte(hex : String) : ByteArray {
        return BigInteger(hex, 16).toByteArray()
    }

    fun bleConnect(bleDevice: BleDevice){
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback(){
            override fun onStartConnect() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDisConnected(isActiveDisConnected: Boolean, device: BleDevice, gatt: BluetoothGatt?, status: Int) {
                Log.e("disconnected", "$isActiveDisConnected ")
                Log.e("disconnected", "$device ")
                Log.e("disconnected", device.device.toString() + " ")
                Log.e("disconnected", device.scanRecord.toString() + " ")
                Log.e("disconnected", device.timestampNanos.toString() + " ")

                ObserverManager.disConnected(device)
                mBleDevice = null
            }

            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt?, status: Int) {
                ObserverManager.connected(bleDevice)
                mBleDevice = bleDevice
            }

            override fun onConnectFail(bleDevice: BleDevice, exception: BleException?) {
                ObserverManager.connectFail(bleDevice)
            }

        })

    }

    fun bleNotify(device: BleDevice){
        BleManager.getInstance().notify(device, SERVICE_UUID, CHARACTERISTIC_UUID, object : BleNotifyCallback(){
            override fun onCharacteristicChanged(data: ByteArray) {
                Log.d("notify", "data(Hex) : " + bytesToHex(data))
                Log.d("notify", "data(Str) : " + hexToString(bytesToHex(data)))
                ObserverManager.notifyData(BleData.getFromData(data))
            }

            override fun onNotifyFailure(exception: BleException?) {

            }

            override fun onNotifySuccess() {
                Log.d("bleNotify", "onNotifySuccess")
//                        Log.e("epionebean, bluetotuiel", EpioneBean.getEpioneData());
//                bleWrite(BleData.getEpioneData())
//                bleWrite(BleData.getEpioneDataSend())
            }

        })
    }

    fun bleWrite(str : String){
        if(mBleDevice == null)
            return

        Log.e("bleWrite", "data(str) : $str")
        Log.e("bleWrite", "data(hex) : " + stringToHex(str))

        BleManager.getInstance().write(mBleDevice, SERVICE_UUID, CHARACTERISTIC_UUID, hexToByte(
            stringToHex(str)), object : BleWriteCallback(){
            override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {
                Log.e("onWriteSuccess", "data(str) : $str")
                Log.e("onWriteSuccess", "data(hex) : " + stringToHex(str))
                Log.e(
                    "onWriteSuccess",
                    current.toString() + " : " + total + " : " + bytesToHex(justWrite)
                )
            }

            override fun onWriteFailure(exception: BleException?) {
                Log.e("onWriteFailure", exception.toString())
            }

        })
    }



}