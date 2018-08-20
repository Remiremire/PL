package com.dbscarlet.pl.main.webService

import com.dbscarlet.applib.contact.TwitterUrl
import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN
import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN_SECRET
import com.dbscarlet.applib.twitterNetwork.HEADER_OAUTH_SECRET
import com.dbscarlet.applib.twitterNetwork.HEADER_OAUTH_TOKEN
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class AuthorizeWebService
    @Inject
    constructor() {
    fun requestToken(): Request<Map<String, String>, *> {
        return OkGo.get<Map<String, String>>(TwitterUrl.REQUEST_TOKEN)
                .headers(HEADER_OAUTH_TOKEN, DEF_OAUTH_TOKEN)
                .headers(HEADER_OAUTH_SECRET, DEF_OAUTH_TOKEN_SECRET)
    }

    fun authorize(token: String, secret: String): Request<String, *> {
        return OkGo.get<String>(TwitterUrl.AUTHORIZE)
                .headers(HEADER_OAUTH_TOKEN, token)
                .headers(HEADER_OAUTH_SECRET, secret)
                .params("oauth_token", token)
    }

    fun accessToken(pinCode: String, token: String, secret: String): Request<Map<String, String>, *> {
        return OkGo.post<Map<String, String>>(TwitterUrl.ACCESS_TOKEN)
                .headers(HEADER_OAUTH_TOKEN, token)
                .headers(HEADER_OAUTH_SECRET, secret)
                .params("oauth_verifier", pinCode)
    }
}