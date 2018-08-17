package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.applib.NetworkError
import com.dbscarlet.applib.twitterNetwork.FormDataCallback
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.webService.AuthorizeWebService
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
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
        val requestToken = webService.requestToken()
        var authorize: Request<String, *>? = null
        val liveData = object : LiveData<Resource<String>>(){
            override fun onActive() {
                requestToken()
            }

            override fun onInactive() {
                val client = OkGo.getInstance().okHttpClient
                OkGo.cancelTag(client, authorize)
                OkGo.cancelTag(client, requestToken)
            }

            private fun requestToken() {
                requestToken.execute(object : FormDataCallback(){
                    override fun onSuccess(response: Response<Map<String, String>>?) {
                        val resultMap = response?.body()
                        if (resultMap == null) {
                            value = Resource.failed(NetworkError.NO_RESPONSE.code, NetworkError.NO_RESPONSE.msg)
                            return
                        }
                        if (resultMap["oauth_callback_confirmed"] == "true") {
//                            OAUTH_TOKEN = resultMap["oauth_token"] ?: DEF_OAUTH_TOKEN
//                            OAUTH_TOKEN_SECRET = resultMap["oauth_token_secret"] ?: DEF_OAUTH_TOKEN_SECRET
                            authorize()
                        }
                    }
                })
            }

            private fun authorize() {

            }

        }
        return liveData
    }
}