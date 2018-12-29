package com.dbscarlet.pl.main.webService

import com.dbscarlet.common.dataResource.ResLiveData
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class AuthorizeWebService
    @Inject
    constructor() {
    fun requestToken(): ResLiveData<Map<String, String>> {
//        return OkGo.get<Map<String, String>>(TwitterUrl.REQUEST_TOKEN)
//                .headers(HEADER_OAUTH_TOKEN, DEF_OAUTH_TOKEN)
//                .headers(HEADER_OAUTH_SECRET, DEF_OAUTH_TOKEN_SECRET)
        TODO()
    }

    fun authorize(token: String, secret: String): ResLiveData<String> {
//        return OkGo.get<String>(TwitterUrl.AUTHORIZE)
//                .headers(HEADER_OAUTH_TOKEN, token)
//                .headers(HEADER_OAUTH_SECRET, secret)
//                .params("oauth_token", token)
        TODO()
    }

    fun accessToken(pinCode: String, token: String, secret: String): ResLiveData<Map<String, String>> {
//        return OkGo.post<Map<String, String>>(TwitterUrl.ACCESS_TOKEN)
//                .headers(HEADER_OAUTH_TOKEN, token)
//                .headers(HEADER_OAUTH_SECRET, secret)
//                .params("oauth_verifier", pinCode)
        TODO()
    }
}