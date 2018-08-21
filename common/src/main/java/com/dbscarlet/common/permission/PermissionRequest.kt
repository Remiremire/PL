package com.dbscarlet.common.permission

import java.util.*

/**
 * Created by Daibing Wang on 2018/7/5.
 */
abstract class PermissionRequest(protected val requestCode: Int) {
    val permissions: List<String> = mutableListOf()
    protected var onExplain: ((AskPermission)-> Unit)? = null
    protected var onGoAppSetting: ((SetAppPermission)-> Unit)? = null
    protected var onAllowed: (()-> Unit)? = null
    protected var onRefused: ((List<String>)-> Unit)? = null

    fun permissions(vararg permissions: String): PermissionRequest {
        this.permissions as MutableList<String>
        this.permissions.clear()
        if (permissions.isNotEmpty()) {
            this.permissions.addAll(Arrays.asList(*permissions))
        }
        return this
    }

    /**
     * 权限被拒绝的回调
     * onRefused的参数为被拒绝的权限
     */
    fun onRefused(onRefused: ((List<String>)-> Unit)?): PermissionRequest {
        this.onRefused = onRefused
        return this
    }

    /**
     * 需要对申请权限的原因作说明的回调
     * onExplain的参数为后续可执行的操作：askPermission弹出权限请求对话框，refused拒绝权限
     */
    fun onExplain(onExplain: ((AskPermission)-> Unit)?): PermissionRequest {
        this.onExplain = onExplain
        return this
    }

    /**
     * 需要跳转应用设置界面申请权限的回调（因为用户曾对权限选择了“不再询问”，无法弹出对话框）
     * onGoAppSetting的参数为后续可执行的操作：goAppSetting跳转App设置界面，cancel取消权限请求
     */
    fun onGoAppSetting(onGoAppSetting: ((SetAppPermission)-> Unit)?): PermissionRequest {
        this.onGoAppSetting = onGoAppSetting
        return this
    }

    /**
     * 发起请求
     * @param onAllowed 权限被授予的回调
     */
    fun execute(onAllowed: (()-> Unit)?) {
        this.onAllowed = onAllowed
        execute()
    }

    /**
     * 发起请求
     */
    abstract fun execute()
}