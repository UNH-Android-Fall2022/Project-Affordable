package com.affordable.utility

import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object Extras {

    fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    fun LinearLayout.setBackground(@DrawableRes id: Int) {
        val sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackgroundDrawable(ContextCompat.getDrawable(context, id));
        } else {
            this.setBackground(ContextCompat.getDrawable(context, id));
        }
    }
}