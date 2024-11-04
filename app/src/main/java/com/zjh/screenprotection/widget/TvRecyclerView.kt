package com.zjh.screenprotection.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class TvRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    init {
        isFocusable = false
        isFocusableInTouchMode = false
        itemAnimator = null
    }
}