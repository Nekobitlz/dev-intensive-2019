package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.TypedValue
import kotlin.math.roundToLong


fun Activity.hideKeyboard() {
    val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.isKeyboardOpen(): Boolean{
    val content = findViewById<View>(android.R.id.content)
    val bounds = Rect()

    content.getWindowVisibleDisplayFrame(bounds)

    val diff = content.height - bounds.height()
    val error = this.convertDpToPx(50F).roundToLong()

    return diff > error
}

fun Activity.isKeyboardClosed(): Boolean = !isKeyboardOpen()

fun Activity.convertDpToPx(dp: Float): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
)

fun Activity.convertPxToDp(px: Float): Float = px / resources.displayMetrics.density