package com.gabrielaangebrandt.blockbuddy.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProcessState(
    @DrawableRes val image: Int,
    @StringRes val text: Int
)