package com.zjh.screenprotection.widget

import com.youth.banner.transformer.AlphaPageTransformer
import com.youth.banner.transformer.DepthPageTransformer
import com.youth.banner.transformer.MZScaleInTransformer
import com.youth.banner.transformer.RotateDownPageTransformer
import com.youth.banner.transformer.RotateUpPageTransformer
import com.youth.banner.transformer.RotateYTransformer
import com.youth.banner.transformer.ScaleInTransformer
import com.youth.banner.transformer.ZoomOutPageTransformer
import com.zjh.screenprotection.entity.BannerSettings

class TransformerOwner {
    val map = mapOf(
        BannerSettings.Transformer.ALPHA to AlphaPageTransformer(),
        BannerSettings.Transformer.DEPTH to DepthPageTransformer(),
        BannerSettings.Transformer.ROTATE_DOWN to RotateDownPageTransformer(),
        BannerSettings.Transformer.ROTATE_UP to RotateUpPageTransformer(),
        BannerSettings.Transformer.ROTATE_Y to RotateYTransformer(),
        BannerSettings.Transformer.SCALE_IN to ScaleInTransformer(),
        BannerSettings.Transformer.ZOOM_OUT to ZoomOutPageTransformer(),
        BannerSettings.Transformer.MZ to MZScaleInTransformer()
    )
}