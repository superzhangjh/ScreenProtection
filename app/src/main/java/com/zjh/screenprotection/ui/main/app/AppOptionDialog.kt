package com.zjh.screenprotection.ui.main.app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.entity.AppEntity
import com.zjh.screenprotection.utils.AppUtils

class AppOptionDialog(
    context: Context,
    private val onAppChange: (app: AppEntity) -> Unit
) : Dialog(context) {
    private val tvName: TextView by lazy { findViewById(R.id.tvName) }
    private val tvLike: TextView by lazy { findViewById(R.id.tvLike) }
    private val tvUninstall: TextView by lazy { findViewById(R.id.tvUninstall) }

    var appEntity: AppEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_app_option)
        tvLike.post {
            tvLike.requestFocus()
        }
    }

    override fun onStart() {
        super.onStart()
        notifyAppEntity()
    }

    private fun notifyAppEntity() {
        appEntity?.run {
            tvName.text = name
            tvLike.text = if (isLike()) "取消喜欢" else "标为喜欢"
            tvLike.setOnClickListener {
                setLike(!isLike())
                onAppChange.invoke(this)
                dismiss()
            }
            tvUninstall.visibility = if (isSystemApp) View.GONE else View.VISIBLE
            tvUninstall.setOnClickListener {
                AppUtils.uninstallApp(context, pkgName)
                dismiss()
            }
        }
    }
}