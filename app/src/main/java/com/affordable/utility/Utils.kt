package com.affordable.utility

import android.R
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.StateListDrawable
import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import java.util.regex.Pattern


class Utils {

    companion object {

        fun isValidEmail(email: String?): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPasswordLength(phone: String): Boolean {
            return phone.length > 5
        }
    }
}

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

fun ensureBackgroundThread(callback: () -> Unit) {
    if (isOnMainThread()) {
        Thread {
            callback()
        }.start()
    } else {
        callback()
    }
}

fun String.isValidEmail(): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return !this.trim().isEmpty() && this.trim().length >=6
}

fun buttonEffect(button: View) {
    button.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                v.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                v.background.clearColorFilter()
                v.invalidate()
            }
        }
        false
    }
}

fun addClickEffect(view: View) {
    val drawableNormal = view.background
    val drawablePressed = view.background.constantState!!.newDrawable()
    drawablePressed.mutate()
    drawablePressed.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_ATOP)
    val listDrawable = StateListDrawable()
    listDrawable.addState(intArrayOf(R.attr.state_pressed), drawablePressed)
    listDrawable.addState(intArrayOf(), drawableNormal)
    view.background = listDrawable
}


