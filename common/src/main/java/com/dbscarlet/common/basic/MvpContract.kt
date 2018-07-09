package com.dbscarlet.common.basic

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import com.dbscarlet.common.util.logE

/**
 * Created by Daibing Wang on 2018/4/23.
 */
interface IView {

}

interface IPresenter<V>: GenericLifecycleObserver {
    var view: V?

    @MainThread
    @CallSuper
    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        when(event) {
            Lifecycle.Event.ON_CREATE-> {
                if (source != null) {
                    try {
                        view = source as V
                    } catch (e: Exception) {
                        logE("IPresenter", "${this::class.java.simpleName} can`t inject view by ${source::class.java.simpleName}")
                    }
                }
                onCreate()
            }
            Lifecycle.Event.ON_START-> onStart()
            Lifecycle.Event.ON_RESUME-> onResume()
            Lifecycle.Event.ON_PAUSE-> onPause()
            Lifecycle.Event.ON_STOP-> onStop()
            Lifecycle.Event.ON_DESTROY-> {
                onDestroy()
                view = null
            }
            Lifecycle.Event.ON_ANY->{}
        }
    }

    fun onCreate(){}
    fun onStart(){}
    fun onResume(){}
    fun onPause(){}
    fun onStop(){}
    fun onDestroy(){}
}

interface BaseView : IView {
    fun toast(msg : String)
    fun toast(@StringRes msgRes : Int)
    fun handleProgress() : ProgressHandler

}

interface ProgressHandler {
    fun show(msg: String? = null, title: String? = null)
    fun update(progress: Int, total: Int)
    fun hide()
    fun dismiss()
}

interface BasePresenter<V: BaseView> : IPresenter<V> {

}

abstract class BaseModel {

}
