package com.zjh.screenprotection.ui.main.app

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.AppEntity
import com.zjh.screenprotection.utils.AppUtils
import com.zjh.screenprotection.utils.ImageUtils

class AppAdapter(
    private val onItemLongClick: (item: AppEntity, position: Int) -> Unit
) : RecyclerView.Adapter<AppAdapter.VH>() {
    var data: List<AppEntity>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var preItemHeight: Float? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false))
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        preItemHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.toInt()
            Log.d("onBindViewHolder", "position:$position preItemHeight:$it lpHeight:${lp.height}")
            holder.itemView.layoutParams = lp
        }
        val item = data?.getOrNull(position) ?: return
        holder.itemView.setOnClickListener {
            AppUtils.openApp(holder.itemView.context, item.pkgName)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick.invoke(item, position)
            return@setOnLongClickListener true
        }
        holder.tvName.text = item.name
        ImageUtils.load(holder.ivIcon, item.icon)
        holder.ivLike.visibility = if (item.isLike()) View.VISIBLE else View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adjustItemSize(rv: RecyclerView, whRatio: Float = 1f) {
        val layoutManager = rv.layoutManager
        if (layoutManager !is GridLayoutManager) {
            throw Exception("只支持GridLayoutManager")
        }
        rv.post {
            val parentWidth = rv.width - rv.paddingStart - rv.paddingEnd
            Log.d("宽度", "parentWidth:$parentWidth")
            if (parentWidth > 0) {
                preItemHeight = (parentWidth / layoutManager.spanCount) * whRatio
                notifyDataSetChanged()
            }
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val ivLike: ImageView = itemView.findViewById(R.id.ivLike)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }
}