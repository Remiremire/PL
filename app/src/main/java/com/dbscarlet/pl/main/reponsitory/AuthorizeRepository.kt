package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.applib.twitterNetwork.*
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.common.dataResource.State
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
        val liveData = object : LiveData<Resource<String>>(){
            override fun onActive() {
                val resource = Resource<String>(State.LOADING, msg = "正在加载")
                value = resource
            }

            override fun onInactive() {

            }

            fun setResource(resource: Resource<String>) {
                value = resource
            }
        }
        webService.requestToken()
                .execute(object: FormDataCallback(){
                    override fun onSuccess(response: Response<Map<String, String>>?) {
                        val resultMap = response?.body()
                        if (resultMap?.get("oauth_callback_confirmed") == "true") {
                            OAUTH_TOKEN = resultMap["oauth_token"] ?: DEF_OAUTH_TOKEN
                            OAUTH_TOKEN_SECRET = resultMap["oauth_token_secret"] ?: DEF_OAUTH_TOKEN_SECRET
                        }
                    }
                })
        return liveData
    }
}