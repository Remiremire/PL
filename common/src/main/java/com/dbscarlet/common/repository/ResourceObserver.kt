package com.dbscarlet.common.repository

import android.arch.lifecycle.Observer

/**
 * Created by Daibing Wang on 2018/7/16.
 */
open class ResourceObserver<T>: Observer<Resource<T>> {
    final override fun onChanged(t: Resource<T>?) {
        when(t?.state) {
            ResourceState.LOADING -> onLoading(t.message, t.progress, t.total)
            ResourceState.SUCCESS -> onSuccess(t.data!!)
            ResourceState.FAILED -> onFailed(t.code, t.message)
            ResourceState.EXCEPTION -> onException(t.cause)
            else -> Unit
        }
    }

    open fun onSuccess(data: T){}

    open fun onLoading(message: String?, progress: Double?, total: Double?) {}

    open fun onFailed(code: Int?, message: String?){}

    open fun onException(cause: Exception?) {}
}