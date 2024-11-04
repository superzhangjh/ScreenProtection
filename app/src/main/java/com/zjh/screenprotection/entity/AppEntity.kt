package com.zjh.screenprotection.entity

import android.graphics.drawable.Drawable

data class AppEntity(
    val name: String,
    val pkgName: String,
    val icon: Drawable?,
    val isSystemApp: Boolean,
    private var isLike: Boolean = false,
    var sort: Int = 0,
) {
    fun setLike(isLike: Boolean) {
        this.isLike = isLike
        sort = if (isLike) 1 else 0
    }

    fun isLike() = isLike
}