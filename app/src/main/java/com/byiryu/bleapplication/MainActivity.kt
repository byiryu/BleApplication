package com.byiryu.bleapplication

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import butterknife.BindView
import butterknife.OnClick
import com.byiryu.bleapplication.data.BleData
import com.byiryu.bleapplication.observer.Observer
import com.byiryu.bleapplication.util.PermissionUtil
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.gun0912.tedpermission.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(),Observer, PermissionListener {
    override fun onPermissionGranted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val TAG = "MainActivity"

    enum class ConnectState(pos:Int){
        Connecting(0), Fail(1), Success(2)

    }

    lateinit var mConnectState: ConnectState
    lateinit var tedPermission : PermissionUtil
    lateinit var mBluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    fun init(){
        tedPermission = PermissionUtil(this)
        tedPermission.setListener(this)

        mConnectState = ConnectState.Connecting

        bluetoothInit()

        if(progressBar != null)
            setPrograssVisible(false)

        scan_msg.setOnClickListener {
            Log.e(TAG, "clickScan")
            Log.e(TAG, "startScan")

            if(mConnectState != ConnectState.Connecting)
                return@setOnClickListener

            if(!isEnableBlueTooth())
                return@setOnClickListener

            BleManager.getInstance().disconnectAllDevice()

            if(tedPermission.isGranted(PermissionUtil.Permissions.Location)){
                if(mConnectState == ConnectState.Connecting)
                    startScan()
            }else{
                tedPermission.checkPermission(PermissionUtil.Permissions.Location)
                return@setOnClickListener
            }
        }
    }

    fun setPrograssVisible(visible : Boolean){
        if (visible)
            progressBar.visibility = VISIBLE
        else
            progressBar.visibility = INVISIBLE
    }

    fun bluetoothInit(){
        if(mConnectState == ConnectState.Connecting){
            BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .operateTimeout = 5000
        }
    }

    fun isEnableBlueTooth() : Boolean{
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(mBluetoothAdapter == null)
            return false

        if(!mBluetoothAdapter.isEnabled){
            startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            return false
        }

        return true
    }

    fun startScan(){
        BleManager.getInstance().scan(object : BleScanCallback(){
            override fun onScanFinished(scanResultList: MutableList<BleDevice>) {
                var str = StringBuilder()
                var list =  ArrayList<BleDevice>()

                for(device in scanResultList){
                    if(device.name != null){
                        list.add(device)
                        Log.e(TAG, "object : " + device.mac)
                        Log.e(TAG, "object : " + device.key)
                        Log.e(TAG, "object : " + device.name)
                        Log.e(TAG, "object : " + device.device)
                        Log.e(TAG, "object : " + device.scanRecord)
                    }
                }

                setPrograssVisible(false)

                if(list.size != 0){
                    var index = 0
                    for(device in list){
                        str.append("[$index] Mac Address :" +device.mac + "\n")
                        index++
                    }
                    result.text = str.toString()
                    scan_msg.text = "검색하기"
                }else{
                    scan_msg.text = "검색된 기기가 없습니다."
                    setPrograssVisible(false)
                }


            }

            override fun onScanStarted(success: Boolean) {
                scan_msg.text ="기기를 검색 하는 중 입니다."
                setPrograssVisible(true)
            }

            override fun onScanning(bleDevice: BleDevice?) {

            }

        })
    }


    override fun connectFail(bleDevice: BleDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connected(bleDevice: BleDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disConnected(bleDevice: BleDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun receiveNotify(bleData: BleData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }





}
