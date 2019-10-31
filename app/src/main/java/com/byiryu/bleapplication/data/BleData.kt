package com.byiryu.bleapplication.data

import com.byiryu.bleapplication.util.BluetoothUtils

class BleData constructor(private var str : String){


    companion object {
        fun getFromData(byteArray: ByteArray ): BleData {
            return BleData(BluetoothUtils.hexToString(BluetoothUtils.bytesToHex(byteArray)))
        }

    }


}