package com.zjh.screenprotection.ui.main.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zjh.screenprotection.entity.AppEntity
import com.zjh.screenprotection.utils.AppUtils
import com.zjh.screenprotection.utils.GsonUtils
import com.zjh.screenprotection.utils.SPUtils
import com.zjh.screenprotection.utils.showToast

class MyAppViewModel(private val app: Application) : AndroidViewModel(app) {
    private val spUtils = SPUtils.getInstance()
    val appEntities = MutableLiveData<MutableList<AppEntity>?>()

    private fun setSortApps(apps: List<AppEntity>?) {
        appEntities.postValue(
            apps?.sortedWith(
                compareByDescending<AppEntity> { it.sort }
                    .thenBy { it.name }
            )?.toMutableList()
        )
    }

    init {
        Log.d("MyAppViewModel", "init")
        showToast("正在加载应用，请稍等")
        Thread().run {
            initApps(AppUtils.getInstalledApps(app))
        }
    }

    private fun initApps(installedApps: List<AppEntity>) {
        val apps = mutableListOf<AppEntity>()
        val json = spUtils.getString(SPUtils.KEY_LIKED_APP)
        val likedAppPkgList = if (json.isNullOrEmpty()) listOf("com.android.settings") else GsonUtils.fromJsonArray(json, String::class.java)
        likedAppPkgList.forEach { pkg ->
            installedApps.find { it.pkgName == pkg }?.setLike(true)
        }
        apps.addAll(installedApps)
        setSortApps(apps)
    }

    /**
     * 更新App，并更新SP数据
     */
    fun setApps(apps: List<AppEntity>?) {
        setSortApps(apps)
        spUtils.putString(SPUtils.KEY_LIKED_APP, GsonUtils.toJson(apps?.filter { it.isLike() }?.map { it.pkgName }))
    }

    fun notifyAppAdd(packageName: String) {
        val addApp = AppUtils.getInstalledApps(app, listOf(packageName)).getOrNull(0)
        if (addApp != null) {
            val apps = appEntities.value ?: mutableListOf()
            apps.add(addApp)
            initApps(apps)
        }
    }

    fun notifyAppRemove(packageName: String) {
        val removeApp = appEntities.value?.find { it.pkgName == packageName }
        appEntities.value?.remove(removeApp)
        setApps(appEntities.value)
    }
}