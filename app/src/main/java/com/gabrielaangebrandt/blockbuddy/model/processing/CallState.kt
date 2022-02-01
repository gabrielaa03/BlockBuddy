package com.gabrielaangebrandt.blockbuddy.model.processing

import androidx.annotation.ColorRes
import com.gabrielaangebrandt.blockbuddy.R

enum class CallState(@ColorRes val color: Int) {
    NORMAL(R.color.green),
    SUSPICIOUS(R.color.orange),
    SCAM(R.color.red)
}