package com.affordable.utility

import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
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

