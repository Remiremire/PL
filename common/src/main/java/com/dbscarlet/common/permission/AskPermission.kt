package com.dbscarlet.common.permission

/**
 * Created by Daibing Wang on 2018/7/6.
 */
interface AskPermission {
    /**
     * 弹出权限请求框
     */
    fun askPermission()

    /**
     * 拒绝权限
     */
    fun refused()
}