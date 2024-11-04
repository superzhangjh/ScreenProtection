package com.zjh.screenprotection.app

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.zjh.screenprotection.ui.main.BannerViewModel

class App : Application() {
    lateinit var bannerViewModel: BannerViewModel

    companion object {
        private lateinit var app: App

        fun setApp(app: App) {
            this.app = app
        }

        fun getApp() = app
    }

    override fun onCreate() {
        super.onCreate()
        setApp(this)
        bannerViewModel = ViewModelProvider.AndroidViewModelFactory(this).create(BannerViewModel::class.java)
    }
}

fun Activity.getApp() = application as App