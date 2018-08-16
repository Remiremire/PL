package com.dbscarlet.common.dataResource

import android.arch.lifecycle.Observer
import android.support.annotation.CallSuper

/**
 * Created by Daibing Wang on 2018/7/16.
 */
open class ResObserver<T>: Observer<Resource<T>> {
    @CallSuper
    override fun onChanged(t: Resource<T>?) {
        when(t?.state) {
            State.LOADING -> onLoading(t, t.msg, t.progress, t.total)
            State.SUCCESS -> onSuccess(t, t.data!!)
            State.FAILED -> onFailed(t, t.code, t.msg)
            State.EXCEPTION -> onException(t, t.cause)
            else -> Unit
        }
    }

    open fun onSuccess(res: Resource<T>, data: T){}

    open fun onLoading(res: Resource<T>, message: String?, progress: Double?, total: Double?) {}

    open fun onFailed(res: Resource<T>, code: Int?, message: String?){}

    open fun onException(res: Resource<T>, cause: Exception?) {}
}