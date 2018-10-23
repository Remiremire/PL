package com.scarlet.lightpermission

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * Created by Daibing Wang on 2018/8/20.
 */
internal val REQUEST_MAP = mutableMapOf<Long, PermRequest>()
private var requestKey: Long = 0

class PermRequest private constructor(val permissions: Array<String>,
                                      internal val onNeedExplain: ((Activity, PermRequest, RequestPermission) -> Unit)?,
                                      internal val onDisable: ((Activity, PermRequest, SetAppPermission) -> Unit)?,
                                      internal val onAllowed: ((Activity, PermRequest) -> Unit)?,
                                      internal val onRefused: ((Activity, PermRequest, List<String>) -> Unit)?) {

    /**
     * 发起请求
     */
    fun execute(activity: Activity, fragmentManager: FragmentManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || permissions.isEmpty()) {
            onAllowed?.invoke(activity, this)
            return
        }
        requestKey++
        val requestKey = requestKey
        REQUEST_MAP[requestKey] = this
        val fragment = LightPermissionFragment()
        val args = Bundle()
        args.putLong("key", requestKey)
        fragment.arguments = args
        fragmentManager.beginTransaction()
                .add(fragment, requestKey.toString())
                .commit()
    }

    class Builder {
        private var activity: Activity
        private var fragmentManager: FragmentManager
        private var permissions: Array<out String> = arrayOf()
        private var onNeedExplain: ((Activity, PermRequest, RequestPermission) -> Unit)? = null
        private var onDisable: ((Activity, PermRequest, SetAppPermission) -> Unit)? = null
        private var onRefused: ((Activity, PermRequest, List<String>) -> Unit)? = null

        constructor(fragment: Fragment) {
            activity = fragment.activity
            fragmentManager = fragment.childFragmentManager
        }

        constructor(activity: FragmentActivity) {
            this.activity = activity
            fragmentManager = activity.supportFragmentManager
        }

        fun permissions(vararg permissions: String): Builder {
            this.permissions = permissions
            return this
        }

        /**
         * 需要对申请权限的原因作说明
         * 第三个参数为后续可执行的操作：askPermission弹出权限请求对话框，refused拒绝权限
         */
        fun onNeedExplain(onExplain: ((Activity, PermRequest, RequestPermission) -> Unit)?): Builder {
            this.onNeedExplain = onExplain
            return this
        }

        /**
         * 权限被禁用（用户拒绝权限，并勾选“不再询问”），无法直接请求权限，只能在应用设置中配置权限
         * 第三个参数为后续可执行的操作：goAppSetting跳转App设置界面，cancel取消权限请求
         */
        fun onDisable(onGoAppSetting: ((Activity, PermRequest, SetAppPermission) -> Unit)?): Builder {
            this.onDisable = onGoAppSetting
            return this
        }

        /**
         * 权限被拒绝
         * 第三个参数为被拒绝的权限
         */
        fun onRefused(onRefused: ((Activity, PermRequest, List<String>) -> Unit)?): Builder {
            this.onRefused = onRefused
            return this
        }

        /**
         * 发起请求
         * @param onAllowed 权限被授予的回调
         */
        fun execute(onAllowed: ((Activity, PermRequest) -> Unit)?) {
            val permRequest = PermRequest(
                    permissions.toList().toTypedArray(),
                    onNeedExplain,
                    onDisable,
                    onAllowed,
                    onRefused)
            permRequest.execute(activity, fragmentManager)
        }
    }
}
