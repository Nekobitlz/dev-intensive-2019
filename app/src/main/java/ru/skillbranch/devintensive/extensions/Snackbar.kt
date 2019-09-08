package ru.skillbranch.devintensive.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setBackgroundDrawable(@DrawableRes drawable: Int): Snackbar = apply { view.setBackgroundResource(drawable) }

fun Snackbar.setTextColor(@ColorInt textColor: Int): Snackbar = apply {
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(textColor)
    }
