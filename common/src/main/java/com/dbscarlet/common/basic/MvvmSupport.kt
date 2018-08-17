package com.dbscarlet.common.basic

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import org.reactivestreams.Publisher

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

fun <T, L: LiveData<T>> L.publisher(lifecycleOwner: LifecycleOwner): Publisher<T> {
    return LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)
}

fun <T, R: Publisher<T>> R.liveData(): LiveData<T> {
    return LiveDataReactiveStreams.fromPublisher(this)
}

fun <T, R> LiveData<T>.map(converter: (T?)-> R): LiveData<R> {
    val result = MutableLiveData<R>()
    observeForever {
        result.postValue(converter.invoke(it))
    }
    return result
}