package com.gabrielaangebrandt.blockbuddy.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


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