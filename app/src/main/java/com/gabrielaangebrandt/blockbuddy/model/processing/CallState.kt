package com.gabrielaangebrandt.blockbuddy.model.processing

import androidx.annotation.ColorRes
import com.gabrielaangebrandt.blockbuddy.R

enum class CallState(@ColorRes val color: Int) {
    CONTACT(R.color.green),
    UNDEFINED(R.color.yellow),
    SUSPICIOUS(R.color.orange),
    SCAM(R.color.red)
}