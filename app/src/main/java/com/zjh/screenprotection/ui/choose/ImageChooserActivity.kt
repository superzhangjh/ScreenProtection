package com.zjh.screenprotection.ui.choose

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R
import com.zjh.screenprotection.app.getApp
import com.zjh.screenprotection.entity.BannerEntity
import com.zjh.screenprotection.ui.choose.adapter.LocalImageAdapter
import com.zjh.screenprotection.utils.FocusHandler

class ImageChooserActivity : AppCompatActivity(R.layout.activity_image_chooser) {
    private val rvImages: RecyclerView by lazy { findViewById(R.id.rvImages) }
    private val btAll: Button by lazy { findViewById(R.id.btAll) }
    private val btApply: Button by lazy { findViewById(R.id.btApply) }

    private val imageVm by lazy { ViewModelProvider(this)[ImageChooseViewModel::class.java] }
    private val bannerVm by lazy { getApp().bannerViewModel }
    private val imageAdapter = LocalImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FocusHandler.attach(this)
        initViews()
        initRvImage()
        initViewModels()

        imageVm.readImages(this, bannerVm.banners.value?.map { it.path })
        btAll.post {
            btAll.requestFocus()
        }
    }

    private fun initViews() {
        btAll.setOnClickListener {
            imageVm.handleSelectAll()
        }
        btApply.setOnClickListener {
            val banners = imageVm.imageEntities.value
                ?.filter { it.isSelected }
                ?.map { BannerEntity(it.path) }
                ?: listOf()
            bannerVm.setBanners(banners)
            finish()
        }
    }

    private fun initRvImage() {
        rvImages.layoutManager = GridLayoutManager(this, 6)
        rvImages.adapter = imageAdapter
        imageAdapter.adjustItemSize(rvImages)
    }

    private fun initViewModels() {
        imageVm.imageEntities.observe(this) {
            imageAdapter.data = it
        }
        bannerVm.banners.observe(this) {
            Log.d("撒打算", "choose ${it.size}")
        }
    }
}