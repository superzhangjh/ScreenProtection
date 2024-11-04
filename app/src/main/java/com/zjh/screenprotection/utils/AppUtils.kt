package com.zjh.screenprotection.utils

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.zjh.screenprotection.entity.AppEntity

object AppUtils {
//    fun isDefaultLauncher(context: Context): Boolean {
//        val intent = Intent(Intent.ACTION_MAIN).apply {
//            addCategory(Intent.CATEGORY_HOME)
//        }
//
//        val packageManager = context.packageManager
//        val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
//
//        for (resolveInfo in resolveInfos) {
//            val packageName = resolveInfo.activityInfo.packageName
//            val className = resolveInfo.activityInfo.name
//            val componentName = ComponentName(packageName, className)
//
//            // 获取用户的默认启动器
//            val preferredActivities = ArrayList<ComponentName>()
//            val intentFilters = ArrayList<IntentFilter>()
//            packageManager.getPreferredActivities(intentFilters, preferredActivities, null)
//
//            if (preferredActivities.isNotEmpty()) {
//                // 检查默认启动器是否为当前应用
//                if (preferredActivities.contains(componentName)) {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }

    fun isDefaultLauncher(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }

        val packageManager = context.packageManager
        val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        for (resolveInfo in resolveInfos) {
            val defaultLauncher = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

            // 检查默认启动器是否为当前应用
            if (defaultLauncher?.activityInfo?.packageName == context.packageName) {
                return true
            }
        }
        return false
    }

    fun openDefaultAppsSettings(context: Context) {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)

        if (activities.isNotEmpty()) {
            context.startActivity(intent)
        } else {
            // 处理找不到活动的情况
            showToast("无法打开设置")
        }

    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    }

    /**
     * 获取已安装应用
     * @param packageNames 指定的包名
     */
    fun getInstalledApps(context: Context, packageNames: List<String>? = null): List<AppEntity> {
        val packageManager = context.packageManager
        val packages: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val apps = mutableListOf<AppEntity>()
        for (appInfo in packages) {
            //检查包名
            if (packageNames?.isNotEmpty() == true) {
                //过滤非指定包名的应用
                if (!packageNames.contains(appInfo.packageName)) {
                    continue
                }
            }
            // 排除无法启动的应用
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                //过滤当前应用
                if (appInfo.packageName == context.packageName) continue
                val appName = appInfo.loadLabel(packageManager).toString()
                val packageName = appInfo.packageName
                val appIcon = appInfo.loadIcon(packageManager)
                // 判断是否是系统应用
                val isSystemApp = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                val isVisible = (appInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0
                Log.d("getInstalledApps", "应用名称: $appName, 包名: $packageName isSystemApp:$isSystemApp isVisible:$isVisible")
                apps.add(AppEntity(appName, packageName, appIcon, isSystemApp))
            }
        }
        return apps
    }

    fun openApp(context: Context, pkgName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(pkgName)
        if (intent == null) {
            showToast(context, "应用不存在")
        } else {
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                showToast(context, "应用不存在")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(context, "打开失败")
            }
        }
    }

    fun uninstallApp(context: Context, pkgName: String) {
        Log.d("卸载APP", pkgName)
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$pkgName")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}