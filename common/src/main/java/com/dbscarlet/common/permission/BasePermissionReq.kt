package com.dbscarlet.common.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.text.TextUtils

/**
 * Created by Daibing Wang on 2018/8/20.
 */
abstract class BasePermissionReq(requestCode: Int) : PermissionRequest(requestCode), AskPermission, SetAppPermission {
    private var showRationale: BooleanArray? = null
    protected abstract val activity: Activity

    final override fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val len = permissions.size
            val permissionArray = arrayOfNulls<String>(len)
            for (i in 0 until len) {
                permissionArray[i] = permissions[i]
            }
            activity.requestPermissions(permissionArray, requestCode)
        } else {
            allowed()
        }
    }

    final override fun refused() {
        runOnUiThread {
            onRequestEnd()
            onRefused?.invoke(permissions.filter {!checkPermission(it)})
        }
    }

    final override fun execute() {
        if (checkPermissionList(permissions)) {
            allowed()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showRationale = shouldShowRationale(permissions)
            }
            var needExplain = false
            showRationale?.forEach {
                if (it) {
                    needExplain = it
                    return@forEach
                }
            }
            if (needExplain) {
                runOnUiThread{ onExplain?.invoke(this@BasePermissionReq) }
            } else {
                askPermission()
            }
        }
    }

    final override fun goSetting() {
        val activity = activity
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivityForResult(intent, requestCode)
    }

    final override fun cancel() {
        refused()
    }

    private fun allowed() {
        runOnUiThread{
            onRequestEnd()
            onAllowed?.invoke()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionList(permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        permissions.forEach {
            if (it.isNotEmpty() && activity.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 弹出系统权限请求框时是否需要说明：
     * App第一次请求该权限：false
     * App非第一次请求权限，并且用户未选择“不再询问”：true
     * App非第一次请求权限，并且用户选择了“不再询问”：false
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun shouldShowRationale(permissions: List<String>): BooleanArray {
        val len = permissions.size
        val result = BooleanArray(len)
        for (i in 0 until len) {
            val p = permissions[i]
            result[i] = !TextUtils.isEmpty(p) && activity.shouldShowRequestPermissionRationale(p)
        }
        return result
    }

    private fun runOnUiThread(runnable: (()->Unit)?) {
        if (runnable != null) {
            activity.runOnUiThread(runnable)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun permissionRequestResult(requestCode: Int) {
        if (this.requestCode != requestCode) return
        if (checkPermissionList(permissions)) {
            allowed()
        } else {
            val showRationaleBefore = showRationale
            val showRationaleAfter = shouldShowRationale(permissions)
            var canPermissionRequest = true
            if (showRationaleBefore != null) {
                val len = permissions.size
                for (i in 0 until len) {
                    //请求前不需要说明，权限被拒绝, 但是拒绝后依然不需要说明，说明用户在这次请求前就选择了“不再询问”，此次请求实际没有弹出对话框
                    if (!showRationaleBefore[i] && !showRationaleAfter[i] && !checkPermission(permissions[i])) {
                        canPermissionRequest = false
                        break
                    }
                }
            }
            if (!canPermissionRequest) {
                runOnUiThread {
                    onGoAppSetting?.invoke(this@BasePermissionReq)
                }
            } else {
                refused()
            }
        }
    }

    fun activityResult(requestCode: Int) {
        if (this.requestCode != requestCode) return
        if (checkPermissionList(permissions)) {
            allowed()
        } else {
            refused()
        }
    }

    protected abstract fun onRequestEnd()
}
