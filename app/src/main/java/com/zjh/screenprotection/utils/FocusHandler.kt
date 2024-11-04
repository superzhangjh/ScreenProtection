package com.zjh.screenprotection.utils

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import com.zjh.screenprotection.BuildConfig
import com.zjh.screenprotection.widget.DpadView

object FocusHandler {
    fun attach(activity: Activity, scale: Float = 1.1f) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        content.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            Log.d("焦点变化", "oldFocus:$oldFocus newFocus:$newFocus")
            oldFocus?.run {
                scaleX = 1f
                scaleY = 1f
            }
            newFocus?.run {
                scaleX = scale
                scaleY = scale
                bringToFront()
            }
        }
        if (BuildConfig.DEBUG) {
            val dpadView = DpadView(activity)
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.END or Gravity.BOTTOM
            content.addView(dpadView, lp)

            dpadView.keyEventHandler = {
                Log.d("FocusHandler", "onKeyEvent action:${it.action } keycode:${it.keyCode}")
                activity.dispatchKeyEvent(it)
            }
        }
    }
}