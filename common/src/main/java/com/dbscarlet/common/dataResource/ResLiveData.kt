package com.dbscarlet.common.dataResource

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class ResLiveData<T> : LiveData<Resource<T>>() {

    fun observe(owner: LifecycleOwner, observer: ResObserver<T>) {
        super.observe(owner, observer)
    }
}