package com.zjh.screenprotection.ui.main.setting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.Option

class SettingOptionAdapter : RecyclerView.Adapter<SettingOptionAdapter.VH>() {
    var data: List<Option>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val ivChecker: ImageView = view.findViewById(R.id.ivChecker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_option, parent, false)
        val vh = VH(view)
        view.setOnClickListener {
            val position = vh.bindingAdapterPosition
            val item = data?.getOrNull(position) ?: return@setOnClickListener
            item.isSelected = !item.isSelected
            notifyItemChanged(position)
        }
        return vh
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data?.getOrNull(position) ?: return
        holder.tvName.text = item.name
        holder.ivChecker.isSelected = item.isSelected
    }
}