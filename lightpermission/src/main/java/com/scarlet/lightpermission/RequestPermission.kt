package com.scarlet.lightpermission

/**
 * Created by Daibing Wang on 2018/8/20.
 */
interface RequestPermission {
    /**
     * 弹出权限请求框
     */
    fun requestPermission()

    /**
     * 拒绝权限
     */
    fun refused()
}
