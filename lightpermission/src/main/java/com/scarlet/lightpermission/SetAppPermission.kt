package com.scarlet.lightpermission

/**
 * Created by Daibing Wang on 2018/8/20.
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
