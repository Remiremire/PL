package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.common.dataResource.State

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class AuthorizeRepository {


    fun requestLoginHtml(): LiveData<Resource<String>> {
        val liveData = object : LiveData<Resource<String>>(){
            override fun onActive() {
                value = Resource(State.LOADING, msg = "正在加载")

            }
        }
        return liveData
    }

}