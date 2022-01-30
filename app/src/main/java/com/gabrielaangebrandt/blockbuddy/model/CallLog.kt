package com.gabrielaangebrandt.blockbuddy.model

data class CallLog(
    val type: CallState,
    val name: String,
    val number: String,
    val time: String
)