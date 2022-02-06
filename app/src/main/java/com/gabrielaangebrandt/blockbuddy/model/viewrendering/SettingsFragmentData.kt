package com.gabrielaangebrandt.blockbuddy.model.viewrendering

data class SettingsFragmentData(
    val instructions: Int,
    val allowOnlyCallsText: Int,
    val allowOnlySmsText: Int,
    val allowContactCallsChecked: Boolean,
    val blockedNumbers: List<String>
)