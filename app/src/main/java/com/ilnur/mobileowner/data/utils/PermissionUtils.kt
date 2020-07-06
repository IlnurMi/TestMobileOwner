package com.ilnur.mobileowner.data.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest.permission
import android.app.Activity
import androidx.core.app.ActivityCompat


class PermissionUtils {
    companion object {
        private var instance: PermissionUtils? = null

        public fun getInstance(): PermissionUtils {
            if (instance == null) {
                instance = PermissionUtils()
            }
            return instance as PermissionUtils
        }
    }

    public fun checkPermission(context: Context, code: Int): Boolean {
        var result: Int = -1
        when (code) {
            ConstantUtils.SEND_SMS_PERMISSION ->
                result = ContextCompat.checkSelfPermission(context, permission.SEND_SMS)
            ConstantUtils.WRITE_STORAGE_PERMISSION ->
                result = ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
            ConstantUtils.CALL_PERMISSION ->
                result = ContextCompat.checkSelfPermission(context, permission.CALL_PHONE)
        }
        if (result == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    fun requestPermission(context: Context, code: Int) {
        when (code) {
            ConstantUtils.SEND_SMS_PERMISSION ->
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(android.Manifest.permission.SEND_SMS),
                    ConstantUtils.SEND_SMS_PERMISSION
                )
            ConstantUtils.WRITE_STORAGE_PERMISSION ->
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    ConstantUtils.WRITE_STORAGE_PERMISSION
                )
            ConstantUtils.CALL_PERMISSION ->
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                    ConstantUtils.CALL_PERMISSION
                )
        }
    }
}