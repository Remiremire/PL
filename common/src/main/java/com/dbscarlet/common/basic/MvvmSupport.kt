package com.dbscarlet.common.basic

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by Daibing Wang on 2018/7/16.
 */
fun <T: FragmentActivity, V: ViewModel> T.createViewModel(vmClazz: Class<V>, key: String? = null): V {
    if (key != null) {
        return ViewModelProviders.of(this).get(key, vmClazz)
    }
    return ViewModelProviders.of(this).get(vmClazz)
}

fun <T: Fragment, V: ViewModel> T.createViewModel(vmClazz: Class<V>, key: String? = null): V {
    if (key != null) {
        return ViewModelProviders.of(this).get(key, vmClazz)
    }
    return ViewModelProviders.of(this).get(vmClazz)
}

fun <T, R> LiveData<T>.map(converter: (T?)-> R?): LiveData<R> {
    val result = MutableLiveData<R>()
    observeForever {
        result.postValue(converter.invoke(it))
    }
    return result
}