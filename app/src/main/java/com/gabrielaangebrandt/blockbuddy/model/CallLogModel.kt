package com.gabrielaangebrandt.blockbuddy.model

data class CallLogModel(
    val type: CallState = CallState.NORMAL,
    val name: String,
    val number: String,
    val time: String
)