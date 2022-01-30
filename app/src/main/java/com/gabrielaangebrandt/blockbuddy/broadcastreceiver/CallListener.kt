package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

interface CallListener {
    fun onCallReceived(number: String)
}