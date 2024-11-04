package com.zjh.screenprotection.ui.choose

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zjh.screenprotection.entity.ImageEntity
import com.zjh.screenprotection.utils.ImageUtils

class ImageChooseViewModel : ViewModel() {
    val imageEntities = MutableLiveData<List<ImageEntity>>()

    fun readImages(context: Context, selectedImages: List<String>?) {
        val images = ImageUtils.loadImagesFromGallery(context).map {
            ImageEntity(it).apply {
                isSelected = selectedImages?.contains(path) ?: false
            }
        }
        imageEntities.value = images
    }

    fun handleSelectAll() {
        imageEntities.value?.let { images ->
            val isSelectedAll = images.find { !it.isSelected } == null
            images.forEach { it.isSelected = !isSelectedAll }
            imageEntities.value = images
        }
    }
}