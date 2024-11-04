package com.zjh.screenprotection.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.youth.banner.Banner
import com.youth.banner.listener.OnPageChangeListener
import com.zjh.screenprotection.R
import com.zjh.screenprotection.app.getApp
import com.zjh.screenprotection.entity.BannerEntity
import com.zjh.screenprotection.entity.BannerSettings
import com.zjh.screenprotection.ui.choose.ImageChooserActivity
import com.zjh.screenprotection.ui.main.app.MyAppFragment
import com.zjh.screenprotection.ui.main.setting.SettingFragment
import com.zjh.screenprotection.utils.FocusHandler
import com.zjh.screenprotection.utils.showToast
import com.zjh.screenprotection.widget.TransformerOwner

@SuppressLint("RestrictedApi")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        const val REQUEST_CODE_STORAGE_PERMISSION = 0x1001
        const val REQUEST_CODE_QUERY_ALL_PACKAGES = 0x1002
    }
    private val tvTip: TextView by lazy { findViewById(R.id.tvTip) }
    private val banner: Banner<BannerEntity, MyBannerAdapter> by lazy { findViewById(R.id.banner) }

    private val bannerVm by lazy { getApp().bannerViewModel }
    private val bannerAdapter = MyBannerAdapter()
    private val transformerOwner = TransformerOwner()
    private val settingFragment = SettingFragment()
    private val myAppFragment = MyAppFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FocusHandler.attach(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        initBannerView()
    }

    private fun initBannerView() {
        banner.setAdapter(bannerAdapter, true)
            .addBannerLifecycleObserver(this)
            .setUserInputEnabled(false)
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    Log.d("banner", "onPageSelected position:$position")
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
    }

    private fun initViewModel() {
        bannerVm.banners.observe(this) {
            val hasBanner = it.isNotEmpty()
            tvTip.visibility = if (hasBanner) View.GONE else View.VISIBLE
            banner.visibility = if (hasBanner) View.VISIBLE else View.GONE
            bannerAdapter.setDatas(it)
        }
        bannerVm.settings.observe(this) {
            setBannerSettings(it)
        }
    }

    fun openImageChoose() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
        } else {
            startActivity(Intent(this, ImageChooserActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CODE_STORAGE_PERMISSION -> openImageChoose()
                REQUEST_CODE_QUERY_ALL_PACKAGES -> openMyFragment()
            }
        } else {
            showToast(this, "获取权限失败")
        }
    }

    private fun setBannerSettings(settings: BannerSettings) {
        banner.setLoopTime(settings.loopTime)
            .setScrollTime(settings.scrollTime)
            .setOrientation(settings.orientation)
//        transformerOwner.map.forEach {
//            banner.removeTransformer(it.value)
//        }
//        settings.transformers.forEach {
//            banner.setPageTransformer(transformerOwner.map[it])
//        }
        banner.setPageTransformer(transformerOwner.map[settings.transformer])
        bannerAdapter.scaleType = settings.scaleType
    }

    private fun switchFragment(fragment: Fragment, show: Boolean) {
        if (show) {
            if (!fragment.isAdded) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit()
                banner.stop()
            }
        } else {
            if (fragment.isAdded) {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
                banner.start()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (getAddedFragment() == null) {
            when (keyCode) {
                KeyEvent.KEYCODE_MENU -> {
                    switchFragment(settingFragment, true)
                    return true
                }
                KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_DOWN,
                KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_CENTER -> {
                    openMyFragment()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun getAddedFragment(): Fragment? {
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        if (fragment?.isAdded == true) {
            return fragment
        }
        return null
    }

    override fun onBackPressed() {
        val fragment = getAddedFragment()
        if (fragment != null) {
            switchFragment(fragment, false)
        } else {
            super.onBackPressed()
        }
    }

    private fun openMyFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(this, Manifest.permission.QUERY_ALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.QUERY_ALL_PACKAGES), REQUEST_CODE_QUERY_ALL_PACKAGES)
        } else {
            switchFragment(myAppFragment, true)
        }
    }
}