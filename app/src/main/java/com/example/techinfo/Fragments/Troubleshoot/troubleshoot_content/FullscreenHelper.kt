package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.app.Activity
import android.view.View
import android.view.WindowManager

class FullscreenHelper(private val activity: Activity) {

    fun enterFullScreen() {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun exitFullScreen() {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
