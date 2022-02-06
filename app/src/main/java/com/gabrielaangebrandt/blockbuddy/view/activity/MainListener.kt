package com.gabrielaangebrandt.blockbuddy.view.activity

interface MainListener {
    fun startProcessingService()
    fun stopProcessingService()
    fun createPermissionAlert()
}