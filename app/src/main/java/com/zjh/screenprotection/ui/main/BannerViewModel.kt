package com.zjh.screenprotection.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zjh.screenprotection.entity.BannerEntity
import com.zjh.screenprotection.entity.BannerSettings
import com.zjh.screenprotection.utils.GsonUtils
import com.zjh.screenprotection.utils.SPUtils

class BannerViewModel(app: Application) : AndroidViewModel(app) {
    private val spUtils = SPUtils.getInstance()
    val banners = MutableLiveData<List<BannerEntity>>()
    val settings = MutableLiveData<BannerSettings>()

    init {
        initBanners()
        initSetting()
    }

    private fun initBanners() {
        val bannerJson = spUtils.getString(SPUtils.KEY_BANNERS)
        banners.value = if (bannerJson.isNullOrEmpty()) {
            listOf()
        } else {
            GsonUtils.fromJsonArray(bannerJson, BannerEntity::class.java)
        }
    }

    fun setBanners(banners: List<BannerEntity>) {
        this.banners.value = banners
        spUtils.putString(SPUtils.KEY_BANNERS, GsonUtils.toJson(banners))
    }

    private fun initSetting() {
        val settingJson = spUtils.getString(SPUtils.KEY_SETTING)
        Log.d("settingJson", "$settingJson")
        settings.value = if (settingJson.isNullOrEmpty()) {
            BannerSettings()
        } else {
            GsonUtils.fromJson(settingJson, BannerSettings::class.java)
        }
    }

    fun setSetting(settings: BannerSettings) {
        this.settings.postValue(settings)
        spUtils.putString(SPUtils.KEY_SETTING, GsonUtils.toJson(settings))
    }
}