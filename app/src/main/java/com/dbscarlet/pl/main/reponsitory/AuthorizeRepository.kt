package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.applib.twitterNetwork.FormDataCallback
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.webService.AuthorizeWebService
import com.lzy.okgo.model.Response
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class AuthorizeRepository
    @Inject
    constructor(
            val webService: AuthorizeWebService
    ){

    fun requestLoginHtml(): LiveData<Resource<String>> {
        val request = webService.requestToken()
        val liveData = object : LiveData<Resource<String>>(){
            override fun onActive() {
                request.execute(object : FormDataCallback(){
                    override fun onSuccess(response: Response<Map<String, String>>?) {

                    }
                })
            }

            override fun onInactive() {

            }

            fun setResource(resource: Resource<String>) {
                value = resource
            }
        }
        return liveData
    }
}