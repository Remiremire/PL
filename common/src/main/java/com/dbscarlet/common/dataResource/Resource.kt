package com.dbscarlet.common.dataResource

import android.arch.lifecycle.LiveData

/**
 * Created by Daibing Wang on 2018/7/16.
 */

/**
 * 数据源[Resource]的LiveData
 */
open class ResLiveData<T> : LiveData<Resource<T>>()

/**
 * 数据源封装，包含了获取数据的过程和错误信息
 */
sealed class Resource<T>(
        private var data: T?
)

/**
 * 加载中
 */
class Loading<T>(
        var msg: String? = null,
        var progress: Double? = null,
        var total: Double? = null,
        var data: T? = null
) : Resource<T>(data)

/**
 * 获取数据成功
 */
class Success<T>(
        var data : T
) : Resource<T>(data)

/**
 * 数据获取失败(比如用户登录密码错误、请求的数据条码已被删除、用户没有访问权限...)
 */
class Fail<T>(
        var msg: String,
        var data: T? = null
) : Resource<T>(data)

/**
 * 数据获取错误(比如网络超时、Json解析错误...)
 */
class Error<T>(
        var msg: String? = null,
        var cause: Throwable,
        var data: T? = null
) : Resource<T>(data)

/**
 * 取消
 */
class Cancel<T>(
        var msg: String? = null,
        var data: T? = null
) : Resource<T>(data)
