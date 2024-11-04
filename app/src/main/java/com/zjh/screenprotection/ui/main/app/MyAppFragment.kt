package com.zjh.screenprotection.ui.main.app

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.screenprotection.R

class MyAppFragment : Fragment(R.layout.fragment_my_app) {
    private val vm by lazy { ViewModelProvider(this)[MyAppViewModel::class.java] }
    private val appOptionDialog by lazy { AppOptionDialog(requireContext()) { _ ->
        vm.setApps(vm.appEntities.value)
    } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyAppFragment", "onViewCreated")
        initVm()
        registerPackageReceiver()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initVm() {
        val rvApp = requireView().findViewById<RecyclerView>(R.id.rvApp)
        val layoutManager = GridLayoutManager(requireContext(), 6)
        rvApp.layoutManager = layoutManager
        val appAdapter = AppAdapter { item, _  ->
            appOptionDialog.appEntity = item
            appOptionDialog.show()
        }
        appAdapter.adjustItemSize(rvApp)
        rvApp.post {
            rvApp.adapter = appAdapter
        }
        vm.appEntities.observe(viewLifecycleOwner) {
            Log.d("MyAppFragment", "observe size:${it?.size} thread:${Thread.currentThread().name}")
            appAdapter.data = it
            resetChildFocus(rvApp, layoutManager)
        }
    }

    /**
     * 重置RV的焦点
     */
    private fun resetChildFocus(rv: RecyclerView, layoutManager: LinearLayoutManager) {
        //为第一个ItemView获取焦点
        rv.smoothScrollToPosition(0)
        rv.postDelayed({
            if (layoutManager.itemCount > 0) {
                layoutManager.getChildAt(0)?.run {
                    post {
                        requestFocus()
                    }
                }
            }
        }, 1000L)
    }

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.data?.encodedSchemeSpecificPart?.let {
                when (intent.action) {
                    Intent.ACTION_PACKAGE_ADDED -> vm.notifyAppAdd(it)
                    Intent.ACTION_PACKAGE_REMOVED -> vm.notifyAppRemove(it)
                }
            }
        }
    }

    private fun registerPackageReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        requireActivity().registerReceiver(packageReceiver, intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MyAppFragment", "onDestroyView")
        requireActivity().unregisterReceiver(packageReceiver)
    }
}