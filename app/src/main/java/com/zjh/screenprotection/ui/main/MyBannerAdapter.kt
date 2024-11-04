package com.zjh.screenprotection.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView.ScaleType
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.zjh.screenprotection.entity.BannerEntity
import com.zjh.screenprotection.utils.ImageUtils

class MyBannerAdapter : BannerImageAdapter<BannerEntity>(listOf()) {
    var scaleType: ScaleType = ScaleType.CENTER_CROP
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindView(
        holder: BannerImageHolder,
        data: BannerEntity?,
        position: Int,
        size: Int
    ) {
        holder.itemView.setBackgroundColor(Color.BLACK)
        holder.imageView.scaleType = scaleType
        ImageUtils.load(holder.imageView, data?.path)
    }
}