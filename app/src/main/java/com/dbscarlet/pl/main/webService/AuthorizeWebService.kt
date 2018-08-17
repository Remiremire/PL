package com.dbscarlet.pl.main.webService

import com.dbscarlet.applib.contact.TwitterUrl
import com.dbscarlet.applib.twitterNetwork.*
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class AuthorizeWebService {
    fun requestToken(): Request<Map<String, String>, *> {
        OAUTH_TOKEN = DEF_OAUTH_TOKEN
        OAUTH_TOKEN_SECRET = DEF_OAUTH_TOKEN_SECRET
        return OkGo.post<Map<String, String>>(TwitterUrl.REQUEST_TOKEN)
    }

    fun authorize(): Request<String, *> {
        return OkGo.get<String>(TwitterUrl.AUTHORIZE)
                .params("oauth_token", OAUTH_TOKEN)
    }

    fun accessToken(pinCode: String): Request<String, *> {
        return OkGo.post<String>(TwitterUrl.ACCESS_TOKEN)
                .params("oauth_verifier", pinCode)
    }
}