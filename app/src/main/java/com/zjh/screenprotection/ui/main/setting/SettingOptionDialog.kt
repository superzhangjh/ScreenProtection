package com.zjh.screenprotection.ui.main.setting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.SystemSetting
import com.zjh.screenprotection.utils.AppUtils

class SettingOptionDialog(context: Context) : Dialog(context) {
    private val tvName: TextView by lazy { findViewById(R.id.tvName) }
    private val rv: RecyclerView by lazy { findViewById(R.id.rv) }
    private val settingAdapter by lazy { SettingOptionAdapter() }

    var setting: SystemSetting? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting_option)
        initViews()
    }

    private fun initViews() {
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = settingAdapter
    }

    override fun onStart() {
        super.onStart()
        notifyEntity()
    }

    private fun notifyEntity() {
        setting?.run {
            tvName.text = getName()
            settingAdapter.data = options
        }
    }
}