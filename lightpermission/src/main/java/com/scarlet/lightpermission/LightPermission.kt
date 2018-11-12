package com.scarlet.lightpermission

import android.Manifest
import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.widget.Toast
import java.util.*

/**
 * Created by Daibing Wang on 2018/10/17.
 */
object LightPermission {

    fun request(activity: FragmentActivity): PermRequest.Builder {
        return request(PermRequest.Builder(activity))
    }

    fun request(fragment: Fragment): PermRequest.Builder {
        return request(PermRequest.Builder(fragment))
    }

    private fun request(builder: PermRequest.Builder): PermRequest.Builder {
        builder.onNeedExplain { context, request, param ->
            val permissionStr = getPermissionString(request.permissions)
            if (TextUtils.isEmpty(permissionStr)) {
                Toast.makeText(context, "需要权限", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "需要权限: $permissionStr", Toast.LENGTH_SHORT).show()
            }
            param.requestPermission()
        }.onDisable { context, request, param ->
            val permissionsStr = getPermissionString(request.permissions)
            val msg = if (TextUtils.isEmpty(permissionsStr))
                "权限被禁用, 是否进行设置?"
            else
                "$permissionsStr 权限被禁用, 是否进行设置?"
            AlertDialog.Builder(context)
                    .setTitle("权限设置")
                    .setMessage(msg)
                    .setOnCancelListener { param.cancel() }
                    .setPositiveButton("确定") { dialog, which -> param.goSetting() }
                    .setNegativeButton("取消") { dialog, which -> param.cancel() }
                    .show()
        }
        return builder
    }

    private fun getPermissionString(permissions: Array<String>): String {
        val strBuilder = StringBuilder("")
        val permissionList = Arrays.asList(*permissions)
        if (permissionList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissionList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            strBuilder.append("存储, ")
        }
        if (permissionList.contains(Manifest.permission.ACCESS_FINE_LOCATION) || permissionList.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            strBuilder.append("位置, ")
        }
        if (permissionList.contains(Manifest.permission.CAMERA)) {
            strBuilder.append("相机, ")
        }
        if (permissionList.contains(Manifest.permission.READ_SMS)
                || permissionList.contains(Manifest.permission.SEND_SMS)
                || permissionList.contains(Manifest.permission.RECEIVE_SMS)
                || permissionList.contains(Manifest.permission.RECEIVE_WAP_PUSH)
                || permissionList.contains(Manifest.permission.RECEIVE_MMS)) {
            strBuilder.append("短信, ")
        }
        if (permissionList.contains(Manifest.permission.RECORD_AUDIO)) {
            strBuilder.append("录音, ")
        }
        if (permissionList.contains(Manifest.permission.READ_PHONE_STATE)
                || permissionList.contains(Manifest.permission.CALL_PHONE)
                || permissionList.contains(Manifest.permission.READ_CALL_LOG)
                || permissionList.contains(Manifest.permission.WRITE_CALL_LOG)
                || permissionList.contains(Manifest.permission.ADD_VOICEMAIL)
                || permissionList.contains(Manifest.permission.USE_SIP)
                || permissionList.contains(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
            strBuilder.append("手机信息(电话), ")
        }
        if (permissionList.contains(Manifest.permission.BODY_SENSORS)) {
            strBuilder.append("传感器, ")
        }
        if (permissionList.contains(Manifest.permission.READ_CALENDAR) || permissionList.contains(Manifest.permission.WRITE_CALENDAR)) {
            strBuilder.append("日历, ")
        }
        if (permissionList.contains(Manifest.permission.READ_CONTACTS)
                || permissionList.contains(Manifest.permission.WRITE_CONTACTS)
                || permissionList.contains(Manifest.permission.GET_ACCOUNTS)) {
            strBuilder.append("联系人, ")
        }
        if (permissionList.contains(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
            strBuilder.append("安装应用, ")
        }
        return if (strBuilder.length > 0) {
            strBuilder.substring(0, strBuilder.length - 2)
        } else ""
    }
}
