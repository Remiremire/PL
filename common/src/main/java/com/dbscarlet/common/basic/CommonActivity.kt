package com.dbscarlet.common.basic

import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.common.permission.AskPermission
import com.dbscarlet.common.permission.PermissionRequest
import com.dbscarlet.common.permission.SetAppPermission
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/4/24.
 */
abstract class CommonActivity : AppCompatActivity(), BaseView, HasFragmentInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector : DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>
    private var progressList: MutableList<ProgressHandler>? = null

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return supportFragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        getPresenters()?.forEach { lifecycle.addObserver(it) }
    }

    abstract fun getPresenters() : Array<IPresenter<*>>?

    override fun onDestroy() {
        super.onDestroy()
        progressList?.forEach { it.hide() }
        getPresenters()?.forEach { lifecycle.removeObserver(it) }
    }

    override fun toast(msgRes: Int) {
        toast(resources.getString(msgRes))
    }

    override fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun handleProgress(): ProgressHandler {
        val progress = object: ProgressHandler {
            override fun dismiss() {
                progressList?.remove(this)
            }

            val progressBar : ProgressBar = ProgressBar(this@CommonActivity)

            override fun show(msg: String?, title: String?) {
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = false
            }

            override fun update(progress: Int, total: Int) {
                if (!progressBar.isIndeterminate) {
                    progressBar.isIndeterminate = true
                }
                progressBar.max = total
                progressBar.progress = progress
            }

            override fun hide() {
                progressBar.visibility = View.GONE
            }
        }
        progressList = progressList ?: mutableListOf()
        progressList?.add(progress)
        return progress
    }


    /////////////////////////动态权限相关////////////////////////////////////////////////
    private var permissionRequestMap: MutableMap<Int, PermissionRequestImpl>? = null

    private inner class PermissionRequestImpl(requestCode: Int): PermissionRequest(requestCode),
        AskPermission, SetAppPermission {

        private var shouldShowRationaleOnAsk = false

        override fun request() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                onAllow()
                return
            }else if(permissions.isEmpty() || checkPermissionsAllowed()) {
                onAllow()
                return
            } else {
                shouldShowRationaleOnAsk = shouldShowRationale()
                if (shouldShowRationaleOnAsk) {
                    runOnUiThread{ onExplain?.invoke(this) }
                } else {
                    askPermission()
                }
            }
        }

        /**
         * 弹出系统权限请求框时是否需要说明：
         *  App第一次请求该权限：false
         *  App非第一次请求权限，并且用户未选择“不再询问”：true
         *  App非第一次请求权限，并且用户选择了“不再询问”：false
         */
        @RequiresApi(Build.VERSION_CODES.M)
        private fun shouldShowRationale(): Boolean {
            for (p in permissions) {
                if (shouldShowRequestPermissionRationale(p)) {
                    return true
                }
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun checkPermissionsAllowed(): Boolean {
            for(p in permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun onRefused() {
            permissionRequestMap?.remove(requestCode)
            val refusedPermissions = mutableListOf<String>()
            for(p in permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    refusedPermissions.add(p)
                }
            }
            runOnUiThread { onRefused?.invoke(refusedPermissions.toTypedArray()) }
        }

        private fun onAllow() {
            permissionRequestMap?.remove(requestCode)
            runOnUiThread { onAllowed?.invoke() }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        internal fun onRequestPermissionResult(requestCode: Int) {
            if (requestCode != this.requestCode) return
            if (checkPermissionsAllowed()) {
                onAllow()
            } else if (!shouldShowRationaleOnAsk && !shouldShowRationale()) {
                //请求前不需要说明，权限被拒绝后依然不需要说明，说明用户在这次请求前就选择了“不再询问”，此次请求实际没有弹出对话框
                if (onGoAppSetting != null) {
                    runOnUiThread { onGoAppSetting?.invoke(this) }
                } else {
                    this.goSetting()
                }
            } else {
                onRefused()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        internal fun onAppSettingResult(requestCode: Int) {
            if (requestCode != this.requestCode) return
            if (checkPermissionsAllowed()) {
                onAllow()
            } else {
                onRefused()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun askPermission() {
            if (permissionRequestMap == null) permissionRequestMap = mutableMapOf()
            permissionRequestMap?.put(requestCode, this)
            requestPermissions(permissions.toTypedArray(), requestCode)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun refused() {
            onRefused()
        }

        override fun goSetting() {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, requestCode)
        }

        override fun cancel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onRefused()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionRequestMap?.get(requestCode)?.onRequestPermissionResult(requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionRequestMap?.get(requestCode)?.onAppSettingResult(requestCode)
        }
    }

    open fun requestPermissions(requestCode: Int): PermissionRequest {
        return PermissionRequestImpl(requestCode)
    }

}