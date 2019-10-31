package com.byiryu.bleapplication.util

import android.Manifest
import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.gun0912.tedpermission.TedPermission.isGranted


class PermissionUtil constructor(private val context: Context){

    lateinit var permissionListener: PermissionListener
    var builder: TedPermission.Builder = TedPermission.with(context)

    enum class Permissions{
        Gallery, Location, Call, SMS, Contract, PhoneState, BlueTooth
    }

    fun setListener(permissionListener: PermissionListener){
        this.permissionListener = permissionListener
    }

    fun checkPermission(permissions: Permissions){
        checkPermission(*PermissionToString(permissions))
    }

    fun checkPermission(vararg permissions : String) {
        builder.setPermissionListener(this.permissionListener)
            .setPermissions(*permissions)
            .check()
    }

    fun isGranted(permissions: Permissions) : Boolean{
        return TedPermission.isGranted(context, *PermissionToString(permissions))
    }

    fun PermissionToString(permissions: Permissions) : Array<String> {

        var permissionArray = arrayOf<String>()

        when(permissions){
            Permissions.Gallery -> permissionArray = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)

            Permissions.Location -> permissionArray = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

            Permissions.BlueTooth -> permissionArray = arrayOf(  Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN)

        }

        return permissionArray

    }

}