package com.zjh.screenprotection.ui.choose.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.ImageEntity
import com.zjh.screenprotection.utils.ImageUtils

class LocalImageAdapter : RecyclerView.Adapter<LocalImageAdapter.LocalImageViewHolder>() {
    var data: List<ImageEntity>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var preItemHeight: Float? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_image, parent, false)
        return LocalImageViewHolder(view)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: LocalImageViewHolder, position: Int) {
        val item = data?.getOrNull(position)

        preItemHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.toInt()
            Log.d("onBindViewHolder", "position:$position preItemHeight:$it lpHeight:${lp.height}")
            holder.itemView.layoutParams = lp
        }

        holder.itemView.setOnClickListener {
            item?.let { it.isSelected = !it.isSelected }
            notifyItemChanged(position)
        }

        ImageUtils.load(holder.iv, item?.path)
        holder.ivChecker.isSelected = item?.isSelected ?: false
    }


    class LocalImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val iv: ImageView = itemView.findViewById(R.id.iv)
        val ivChecker: ImageView = itemView.findViewById(R.id.iv_checker)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adjustItemSize(rv: RecyclerView, whRatio: Float = 1f) {
        val layoutManager = rv.layoutManager
        if (layoutManager !is GridLayoutManager) {
            throw Exception("只支持GridLayoutManager")
        }
        rv.post {
            val parentWidth = rv.width
            Log.d("宽度", "parentWidth:$parentWidth")
            if (parentWidth > 0) {
                preItemHeight = (parentWidth / layoutManager.spanCount) * whRatio
                notifyDataSetChanged()
            }
        }
    }
}