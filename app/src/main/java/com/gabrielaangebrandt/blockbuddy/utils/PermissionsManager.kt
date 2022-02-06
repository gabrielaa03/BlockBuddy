package com.gabrielaangebrandt.blockbuddy.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.gabrielaangebrandt.blockbuddy.view.fragment.callback.PermissionsCallback

class PermissionsManager(val context: Context) {
    private val missingPermissions = mutableListOf<String>()
    private val callLogPermission = Manifest.permission.READ_CALL_LOG
    private val callPhonePermission = Manifest.permission.CALL_PHONE
    private val accessContactsPermission = Manifest.permission.READ_CONTACTS

    private lateinit var listener: PermissionsCallback

    // The listener could also be set with regular setter,
    // but I like this approach more since
    // the listener is not exposed to other classes
    fun setListener(listener: PermissionsCallback) {
        this.listener = listener
    }

    private fun checkPermissionState(permission: String) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            missingPermissions.add(permission)
        }
    }

    fun requestPermissions(changeRequested: Boolean) {
        checkPermissionState(callLogPermission)
        checkPermissionState(callPhonePermission)
        checkPermissionState(accessContactsPermission)

        if (missingPermissions.isEmpty()) {
            listener.onPermissionsGranted(changeRequested)
        } else {
            listener.onPermissionsMissing(missingPermissions.toTypedArray())
            missingPermissions.clear()
        }
    }
}