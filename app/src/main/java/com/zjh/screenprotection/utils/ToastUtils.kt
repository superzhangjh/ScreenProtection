package com.zjh.screenprotection.utils

import android.content.Context
import android.widget.Toast
import com.zjh.screenprotection.app.App

fun showToast(content: String) {
    showToast(App.getApp(), content)
}

fun showToast(context: Context, content: String) {
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}