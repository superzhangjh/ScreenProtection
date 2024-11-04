package com.zjh.screenprotection.ui.main.setting

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.SystemSetting

class SettingAdapter(val data: List<SystemSetting>?) : RecyclerView.Adapter<SettingAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return VH(view)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        val setting = data?.getOrNull(position) ?: return
        val bindingPosition = holder.bindingAdapterPosition
        holder.tvName.text = setting.getName()
        holder.tvOptionName.text = setting.options.filter { it.isSelected }.joinToString { it.name }

        holder.ivLeft.setOnClickListener { handleSingleOptionChange(setting, false, bindingPosition) }
        holder.ivRight.setOnClickListener { handleSingleOptionChange(setting, true, bindingPosition) }
        holder.ivLeft.visibility = if (setting.isMulti) View.INVISIBLE else View.VISIBLE
        holder.ivRight.visibility = if (setting.isMulti) View.INVISIBLE else View.VISIBLE

        holder.itemView.setOnKeyListener(if (setting.isMulti) null else object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            handleSingleOptionChange(setting, false, bindingPosition)
                            return true
                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            handleSingleOptionChange(setting, true, bindingPosition)
                            return true
                        }
                    }
                }
                return false
            }
        })

        holder.itemView.setOnClickListener {
            if (setting.isMulti) {
                val dialog = SettingOptionDialog(holder.itemView.context)
                dialog.setting = setting
                dialog.setOnDismissListener {
                    notifyItemChanged(bindingPosition)
                }
                dialog.show()
            }
        }
    }

    private fun handleSingleOptionChange(setting: SystemSetting?, next: Boolean, position: Int) {
        if (setting == null) return
        val selectedIndex = setting.options.indexOfFirst { it.isSelected }
        val optionSize = setting.options.size
        val nextIndex = (selectedIndex + optionSize + (if (next) 1 else -1)) % optionSize
        setting.options.forEachIndexed { index, option ->
            option.isSelected = index == nextIndex
        }
        notifyItemChanged(position)
    }

    class VH(itemView: View) : ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvOptionName: TextView = itemView.findViewById(R.id.tvOptionName)
        val ivLeft: ImageView = itemView.findViewById(R.id.ivLeft)
        val ivRight: ImageView = itemView.findViewById(R.id.ivRight)
    }
}