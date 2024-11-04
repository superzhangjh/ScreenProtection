package com.zjh.screenprotection.entity

import android.widget.ImageView
import com.youth.banner.Banner

data class BannerSettings(
    //loopTime需大于scrollTime
    var loopTime: Long = 3000L,
    val scrollTime: Int = 800,
    val orientation: Int = Banner.VERTICAL,
    val transformer: Transformer = Transformer.ALPHA,
    val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
) {
    enum class Transformer(val des: String) {
        ALPHA("淡入淡出"),
        DEPTH("景深"),
        ROTATE_DOWN("向下旋转"),
        ROTATE_UP("向上旋转"),
        ROTATE_Y("Y轴旋转"),
        SCALE_IN("缩放进入"),
        ZOOM_OUT("变焦退出"),
        MZ("魅族效果")
    }
}