package com.dbscarlet.common.permission

/**
 * Created by Daibing Wang on 2018/7/5.
 */
abstract class PermissionRequest(internal val requestCode: Int) {
    protected var permissions = mutableListOf<String>()
    protected var onExplain: ((AskPermission) -> Unit)? = null
    protected var onGoAppSetting: ((SetAppPermission) -> Unit)? = null
    protected var onAllowed: (() -> Unit)? = null
    protected var onRefused: ((Array<String>) -> Unit)? = null

    fun permission(vararg permission: String): PermissionRequest {
        permission.forEach { permissions.add(it) }
        return this
    }

    /**
     * 权限被授予的回调
     */
    fun onAllowed(onAllowed: (() -> Unit)?): PermissionRequest {
        this.onAllowed = onAllowed
        return this
    }

    /**
     * 权限被拒绝的回调，参数为被拒绝的权限
     */
    fun onRefused(onRefused: ((Array<String>) -> Unit)?): PermissionRequest {
        this.onRefused = onRefused
        return this
    }

    /**
     * 需要对申请权限的原因作说明的回调
     * 参数为后续可执行的操作：askPermission弹出权限请求对话框，refused拒绝权限
     */
    fun onExplain(onExplain: ((AskPermission) -> Unit)?): PermissionRequest {
        this.onExplain = onExplain
        return this
    }

    /**
     * 需要跳转应用设置界面申请权限的回调（因为用户曾对权限选择了“不再询问”，无法弹出对话框）
     * 参数为后续可执行的操作：goAppSetting跳转App设置界面，cancel取消权限请求
     */
    fun onGoAppSetting(onGoAppSetting: ((SetAppPermission) -> Unit)?): PermissionRequest {
        this.onGoAppSetting = onGoAppSetting
        return this
    }

    abstract fun request()
}