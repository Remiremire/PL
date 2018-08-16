package com.dbscarlet.pl.main.webService

import com.dbscarlet.applib.contact.TwitterUrl
import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN
import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN_SECRET
import com.dbscarlet.applib.twitterNetwork.OAUTH_TOKEN
import com.dbscarlet.applib.twitterNetwork.OAUTH_TOKEN_SECRET
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class AuthorizeWebService {
    fun requestToken(): Request<String, *> {
        OAUTH_TOKEN = DEF_OAUTH_TOKEN
        OAUTH_TOKEN_SECRET = DEF_OAUTH_TOKEN_SECRET
        return OkGo.post<String>(TwitterUrl.REQUEST_TOKEN)
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