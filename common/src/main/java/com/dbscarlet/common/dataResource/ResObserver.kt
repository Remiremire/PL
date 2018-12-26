package com.dbscarlet.common.dataResource

import android.arch.lifecycle.Observer
import android.support.annotation.CallSuper

/**
 * Created by Daibing Wang on 2018/7/16.
 */
abstract class ResObserver<T>: Observer<Resource<T>> {
    @CallSuper
    override fun onChanged(res: Resource<T>?) {
        when(res) {
            is Loading -> onLoading(res)
            is Success -> onSuccess(res)
            is Fail -> onFail(res)
            is Error -> onError(res)
            is Cancel -> onCancel(res)
            null -> {  }
        }
    }

    /**
     * 加载中
     */
    open fun onLoading(res: Loading<T>) {}
    /**
     * 数据获取成功
     */
    abstract fun onSuccess(res: Success<T>)
    /**
     * 数据获取失败(比如用户登录密码错误、请求的数据条码已被删除、用户没有访问权限...)
     */
    open fun onFail(res: Fail<T>) {}
    /**
     * 数据获取错误(比如网络超时、Json解析错误...)
     */
    open fun onError(res: Error<T>) {}
    /**
     * 已取消
     */
    open fun onCancel(res: Cancel<T>) {}
}