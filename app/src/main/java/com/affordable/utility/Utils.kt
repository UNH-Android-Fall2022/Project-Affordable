package com.affordable.utility

import android.os.Looper
import android.text.TextUtils
import android.util.Patterns


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


