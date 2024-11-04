package com.zjh.screenprotection.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object ImageUtils {
    fun load(iv: ImageView, url: Any?) {
        Glide.with(iv.context).load(url).into(iv)
    }

    fun copyImageToAppDirectory(context: Context, imageUri: Uri): Boolean {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            // 获取应用内部存储目录
            val appDirectory: File = File(context.filesDir, "images")
            if (!appDirectory.exists()) {
                appDirectory.mkdirs() // 创建目录
            }

            // 创建输出文件
            val outputFile = File(appDirectory, "copied_image.jpg")
            outputStream = FileOutputStream(outputFile)

            // 获取输入流
            inputStream = context.contentResolver.openInputStream(imageUri) ?: return false
            val buffer = ByteArray(1024)
            var length: Int

            // 复制文件
            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream.write(buffer, 0, length)
            }

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            // 处理异常
            return false
        } finally {
            // 关闭流
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun loadImagesFromGallery(context: Context): List<String> {
        val imagePaths = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(uri, projection, null, null, null)
        Log.d("读取信息", "null: ${cursor == null}")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val imagePath = cursor.getString(columnIndex)
                imagePaths.add(imagePath)
            }
            cursor.close()
        }
        return imagePaths
    }
}