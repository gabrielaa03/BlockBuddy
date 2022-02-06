package com.gabrielaangebrandt.blockbuddy.view.fragment.callback

interface PermissionsCallback {
    fun onPermissionsGranted(changeRequested: Boolean)
    fun onPermissionsMissing(missingPermissions: Array<String>)
}