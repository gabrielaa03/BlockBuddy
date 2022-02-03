package com.gabrielaangebrandt.blockbuddy.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*


// LiveData observing extensions
inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onChange: (T) -> Unit) =
    observe(owner) { it?.run { onChange(it) } }

inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onChange: () -> Unit) =
    observe(owner) { it?.run { onChange() } }

// View extensions
fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
fun Long.convertToDate(): String {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return sdf.format(Date(this))
}