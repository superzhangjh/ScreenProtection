package com.zjh.screenprotection.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.BannerEntity
import com.zjh.screenprotection.utils.ImageUtils

class MyBannerAdapter : BannerAdapter<BannerEntity, MyBannerAdapter.MyBannerViewHolder>(listOf()) {
    private val deviceInfo: String = "${Build.BRAND} ${Build.MODEL}"

    var scaleType: ScaleType = ScaleType.CENTER_INSIDE
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindView(
        holder: MyBannerViewHolder,
        data: BannerEntity?,
        position: Int,
        size: Int
    ) {
        holder.itemView.setBackgroundColor(Color.BLACK)
        holder.iv.scaleType = scaleType
        ImageUtils.load(holder.iv, data?.path)
//        holder.itemView.context.resources.displayMetrics.let {
//            Log.d("屏幕宽高", "width:${it.widthPixels} height:${it.heightPixels}")
//        }
//        Glide.with(holder.iv.context)
//            .load(data?.path)
//            .fitCenter()
//            .into(object : SimpleTarget<Drawable>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    holder.iv.background = resource
////                    val viewWidth = holder.itemView.width
////                    val viewHeight = holder.itemView.height
////                    if (viewWidth <= 0 && viewHeight <= 0) return
////
////                    val imageWidth = resource.intrinsicWidth
////                    val imageHeight = resource.intrinsicHeight
////
////                    val viewAspectRatio = viewWidth.toFloat() / viewHeight
////                    val imageAspectRatio = imageWidth.toFloat() / imageHeight
////
////                    Log.d("itemView大小", "width:${holder.itemView.width} height:${holder.itemView.height}")
////                    Log.d("加载图片", "width:${resource.intrinsicWidth} height:${resource.intrinsicHeight}")
////                    Log.d("比例", "viewAspectRatio:$viewAspectRatio imageAspectRatio:$imageAspectRatio")
////
////                    val lp = holder.iv.layoutParams
////                    if (imageAspectRatio > viewAspectRatio) {
////                        //图片宽度大于view宽度，将宽度铺满，高度缩放
////                        lp.width = viewWidth
////                        lp.height = (viewWidth / imageAspectRatio).toInt()
////                    } else {
////                        //图片高度大于view高度, 将高度铺满，宽度缩放
////                        lp.width = (viewHeight * imageAspectRatio).toInt()
////                        lp.height = viewHeight
////                    }
////                    Log.d("调整的iv大小", "width:${lp.width} height:${lp.height}")
////                    holder.iv.layoutParams = lp
////                    holder.iv.scaleType = ScaleType.FIT_XY
////                    holder.iv.setImageDrawable(resource)
//                }
//            })
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): MyBannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_banner, parent, false)
        val vh = MyBannerViewHolder(view)
        //小米电视显示不全，大概只能显示90%，需要手动调整View的大小
        if (deviceInfo.startsWith("Xiaomi MiTV")) {
            parent.post {
                if (parent.width > 0 && parent.height > 0) {
                    val lp = vh.iv.layoutParams
                    lp.width = (parent.width * .9f).toInt()
                    lp.height = (parent.height * .9f).toInt()
                    vh.iv.layoutParams = lp
                    Log.d("调整后的view大小", "w:${lp.width} h:${lp.height}")
                }
            }
        }
        return vh
    }

    class MyBannerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val iv = view.findViewById<ImageView>(R.id.iv)
    }
}