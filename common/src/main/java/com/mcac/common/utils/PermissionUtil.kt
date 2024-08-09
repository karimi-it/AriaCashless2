package com.mcac.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionUtil
{
    fun checkPermission(context: Context, permission: String): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(context: Context, requestCode: Int, vararg permission: String) {
        ActivityCompat.requestPermissions(context as AppCompatActivity, permission, requestCode)
    }

    fun requestPermissionFromFragment(
        fragment: Fragment,
        requestCode: Int,
        vararg permissions: String
    ) {
        fragment.requestPermissions(permissions, requestCode)
    }
}
