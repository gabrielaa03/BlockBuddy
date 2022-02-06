package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

interface CallListener {
    fun onCallReceived(number: String)
    fun onCallFinished(number: String)
}