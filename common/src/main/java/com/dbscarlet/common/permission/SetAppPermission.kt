package com.dbscarlet.common.permission

/**
 * Created by Daibing Wang on 2018/7/6.
 */
interface SetAppPermission {
    /**
     * 前往App设置界面，进行权限设置
     */
    fun goSetting()

    /**
     * 取消权限申请
     */
    fun cancel()
}