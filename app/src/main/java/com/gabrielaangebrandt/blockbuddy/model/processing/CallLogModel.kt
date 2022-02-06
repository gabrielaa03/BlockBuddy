package com.gabrielaangebrandt.blockbuddy.model.processing

data class CallLogModel(
    val type: Int,
    var name: String,
    val number: String,
    val time: String,
    val state: CallState
)