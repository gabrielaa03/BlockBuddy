package com.gabrielaangebrandt.blockbuddy.view.fragment.callback

interface PermissionsCallback {
    fun onPermissionsGranted()
    fun onPermissionsMissing(missingPermissions: Array<String>)
}