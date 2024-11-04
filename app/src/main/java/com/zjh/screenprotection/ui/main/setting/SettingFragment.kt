package com.zjh.screenprotection.ui.main.setting

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.app.App.Companion.getApp
import com.zjh.screenprotection.entity.SystemSetting
import com.zjh.screenprotection.ui.main.MainActivity
import com.zjh.screenprotection.utils.AppUtils

class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val bannerVm by lazy { getApp().bannerViewModel }
    private val settingAdapter by lazy { SettingAdapter(SystemSetting.fromBannerSettings(bannerVm.settings.value)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        view.findViewById<View>(R.id.btSelect).run {
            setOnClickListener {
                (requireActivity() as MainActivity).openImageChoose()
            }
            nextFocusUpId = 0
            post {
                requestFocus()
            }
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvSetting)
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv.adapter = settingAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingAdapter.data?.let {
            bannerVm.setSetting(SystemSetting.toBannerSetting(it))
        }
    }

    private fun updateLauncherButton() {
        val btLauncher = requireView().findViewById<Button>(R.id.btLauncher)
        val isDefaultLauncher = AppUtils.isDefaultLauncher(requireContext())
        btLauncher.text = if (isDefaultLauncher) "取消桌面" else "设为桌面"
        btLauncher.setOnClickListener {
            AppUtils.openDefaultAppsSettings(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        updateLauncherButton()
    }
}