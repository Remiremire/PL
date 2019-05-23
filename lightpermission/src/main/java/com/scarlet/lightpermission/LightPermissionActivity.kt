package com.scarlet.lightpermission

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import java.util.*





/**
 * Created by Daibing Wang on 2019/5/22.
 */
private const val PERMISSION_REQUEST_CODE = 853
@TargetApi(Build.VERSION_CODES.M)
class LightPermissionActivity: AppCompatActivity(), RequestPermission, SetAppPermission {
    private val requestKey by lazy { intent.getLongExtra("key", 0L) }
    private val request by lazy {
        REQUEST_MAP.remove(requestKey)
    }
    private var showRationale: BooleanArray? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.permAct)
        super.onCreate(savedInstanceState)
        startCheckPermission()
    }

    private fun startCheckPermission() {
        val request = request ?: return finish()
        if (checkPermissions(request.permissions)) {
            allowed()
        } else {
            val showRationale = shouldShowRationale(request.permissions)
            var needExplain = false
            for (show in showRationale) {
                if (show) {
                    needExplain = true
                    break
                }
            }
            this.showRationale = showRationale
            if (needExplain) {
                handler.post {
                    request.onNeedExplain?.invoke(this, request, this)
                }
            } else {
                requestPermission()
            }
        }
    }

    override fun requestPermission() {
        val permissions = request?.permissions
        if (permissions != null) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        } else {
            allowed()
        }
    }

    private fun allowed() {
        val request = request ?: return finish()
        finish()
        handler.post {
            request.onAllowed?.invoke(this, request)
        }
    }

    override fun refused() {
        val request = request ?: return finish()
        finish()
        handler.post {
            val refusedPermissions = ArrayList<String>()
            for (p in request.permissions) {
                if (!checkPermission(p)) {
                    refusedPermissions.add(p)
                }
            }
            request.onRefused?.invoke(this, request, refusedPermissions)
        }
    }

    override fun goSetting() {
        val request = request ?: return finish()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivityForResult(intent, PERMISSION_REQUEST_CODE)
    }

    override fun cancel() {
        refused()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val request = request ?: return finish()
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return
        }
        val perms = request.permissions
        if (checkPermissions(perms)) {
            allowed()
        } else {
            val showRationaleAfter = shouldShowRationale(perms)
            var permDisable = false
            val len = perms.size
            val showRationale = this.showRationale
            if (showRationale != null) {
                for (i in 0 until len) {
                    //请求前不需要说明，权限被拒绝, 但是拒绝后依然不需要说明，说明用户在这次请求前就选择了“不再询问”，此次请求实际没有弹出对话框
                    if (!showRationale[i] && !showRationaleAfter[i] && !checkPermission(perms[i])) {
                        permDisable = true
                        break
                    }
                }
            }
            if (permDisable && request.onDisable != null) {
                //权限被禁止时的操作
                handler.post { request.onDisable.invoke(this, request, this) }
            } else {
                refused()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val request = request ?: return finish()
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return
        }
        if (checkPermissions(request.permissions)) {
            allowed()
        } else {
            refused()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        for (p in permissions) {
            if (!TextUtils.isEmpty(p) && checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
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
    private fun shouldShowRationale(permissions: Array<String>): BooleanArray {
        val len = permissions.size
        val result = BooleanArray(len)
        for (i in 0 until len) {
            val p = permissions[i]
            result[i] = !TextUtils.isEmpty(p) && shouldShowRequestPermissionRationale(p)
        }
        return result
    }
}