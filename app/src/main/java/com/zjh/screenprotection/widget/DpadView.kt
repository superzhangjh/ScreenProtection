package com.zjh.screenprotection.widget

import android.app.Instrumentation
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.zjh.screenprotection.R

class DpadView(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {
    var keyEventHandler: ((KeyEvent) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_keyboard, this, true)
        setOnClickListener {  }
        initKey(R.id.enter, KeyEvent.KEYCODE_DPAD_CENTER)
        initKey(R.id.up, KeyEvent.KEYCODE_DPAD_UP)
        initKey(R.id.right, KeyEvent.KEYCODE_DPAD_RIGHT)
        initKey(R.id.down, KeyEvent.KEYCODE_DPAD_DOWN)
        initKey(R.id.left, KeyEvent.KEYCODE_DPAD_LEFT)
        initKey(R.id.menu, KeyEvent.KEYCODE_MENU)
        initKey(R.id.back, KeyEvent.KEYCODE_BACK)
        alpha = 0.3f
    }

    private fun initKey(vId: Int, keyCode: Int) {
        findViewById<View>(vId).setOnClickListener {
            keyEventHandler?.invoke(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
            keyEventHandler?.invoke(KeyEvent(KeyEvent.ACTION_UP, keyCode))
        }
    }

    private fun simulateKeyPress(keyCode: Int) {
        val instrumentation = Instrumentation()
        instrumentation.sendKeyDownUpSync(keyCode)
    }
}