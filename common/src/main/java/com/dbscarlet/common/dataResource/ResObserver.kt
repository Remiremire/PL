package com.dbscarlet.common.dataResource

import android.arch.lifecycle.Observer
import android.support.annotation.CallSuper

/**
 * Created by Daibing Wang on 2018/7/16.
 */
abstract class ResObserver<T>: Observer<Resource<T>> {
    @CallSuper
    override fun onChanged(t: Resource<T>?) {
        when(t?.state) {
            State.LOADING -> onLoading(t, t.msg, t.progress, t.total)
            State.SUCCESS -> onSuccess(t, t.data!!)
            State.FAILED -> onFailed(t, t.code, t.msg, t.cause)
            State.EXCEPTION -> onException(t, t.code, t.msg, t.cause)
            else -> Unit
        }
    }

    abstract fun onSuccess(res: Resource<T>, data: T)

    open fun onLoading(res: Resource<T>, msg: String?, progress: Double?, total: Double?) {}

    open fun onFailed(res: Resource<T>, code: Int?, msg: String?, cause: Throwable?){}

    open fun onException(res: Resource<T>, code: Int?, msg: String?, cause: Throwable?) {}
}