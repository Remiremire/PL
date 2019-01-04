package com.dbscarlet.pl.main.webService

import retrofit2.http.GET

/**
 * Created by Daibing Wang on 2019/1/4.
 */
interface TwitterApi {

    @GET("oauth/request_token")
    fun requestToken()
}