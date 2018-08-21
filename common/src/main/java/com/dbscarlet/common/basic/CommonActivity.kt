package com.dbscarlet.common.basic

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.SparseArray
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.common.permission.BasePermissionReq
import com.dbscarlet.common.permission.PermissionRequest
import com.dbscarlet.common.util.toastShort
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/4/24.
 */
abstract class CommonActivity : AppCompatActivity(), HasFragmentInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector : DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

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

    }

    /////////////////////////动态权限相关////////////////////////////////////////////////
    private val reqMapKey = ""
    private val permissionReqMap: SparseArray<PermissionRequestImpl> by lazy {
        SparseArray<PermissionRequestImpl>()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionRequest = permissionReqMap.get(requestCode)
            permissionRequest?.permissionRequestResult(requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionRequest = permissionReqMap.get(requestCode)
            permissionRequest?.activityResult(requestCode)
        }
    }

    private inner class PermissionRequestImpl constructor(requestCode: Int) : BasePermissionReq(requestCode) {

        override val activity: Activity
            get() = this@CommonActivity

        init {
            permissionReqMap.put(requestCode, this)
        }

        override fun onRequestEnd() {
            permissionReqMap.remove(requestCode)
        }
    }

    /**
     * 检查并申请动态权限
     * @param requestCode
     * @return
     */
    fun requestPermissions(requestCode: Int): PermissionRequest {
        val request = PermissionRequestImpl(requestCode)
        request.onExplain{
            val permissionStr = getPermissionString(request.permissions)
            if (TextUtils.isEmpty(permissionStr)) {
                toastShort("需要权限", this@CommonActivity)
            } else {
                toastShort("需要权限: $permissionStr", this@CommonActivity)
            }
            it.askPermission()
        }.onGoAppSetting { it ->
            val permissionsStr = getPermissionString(request.permissions)
            val msg = if (TextUtils.isEmpty(permissionsStr))
                "权限被禁用, 是否进行设置?"
            else
                "$permissionsStr 权限被禁用, 是否进行设置?"
            AlertDialog.Builder(this@CommonActivity)
                    .setTitle("权限设置")
                    .setMessage(msg)
                    .setOnCancelListener{ it.cancel() }
                    .setPositiveButton("确定") { _, _ -> it.goSetting() }
                    .setNegativeButton("取消") { _, _ -> it.cancel() }.show()
        }
        return request
    }

    private fun getPermissionString(permissions: List<String>): String {
        val strBuilder = StringBuilder("")
        if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            strBuilder.append("存储, ")
        } else if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) || permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            strBuilder.append("位置, ")
        } else if (permissions.contains(Manifest.permission.CAMERA)) {
            strBuilder.append("相机, ")
        } else if (permissions.contains(Manifest.permission.READ_SMS)) {
            strBuilder.append("短信, ")
        } else if (permissions.contains(Manifest.permission.RECORD_AUDIO)) {
            strBuilder.append("录音")
        } else if (permissions.contains(Manifest.permission.READ_PHONE_STATE)) {
            strBuilder.append("手机信息, ")
        }
        return if (strBuilder.isNotEmpty()) {
            strBuilder.substring(0, strBuilder.length - 2)
        } else ""
    }

}