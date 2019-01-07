package com.dbscarlet.pl.main.webService

import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN
import com.dbscarlet.applib.twitterNetwork.DEF_OAUTH_TOKEN_SECRET
import com.dbscarlet.applib.twitterNetwork.HEADER_OAUTH_SECRET
import com.dbscarlet.applib.twitterNetwork.HEADER_OAUTH_TOKEN
import com.diwinet.xhs.tweets.bean.TimelineRequest
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by Daibing Wang on 2019/1/4.
 */
interface TwitterApi {

    @GET("oauth/request_token")
    fun requestToken(
            @Header(HEADER_OAUTH_TOKEN) token : String = DEF_OAUTH_TOKEN,
            @Header(HEADER_OAUTH_SECRET) secret: String = DEF_OAUTH_TOKEN_SECRET
    ): Flowable<String>

    @GET("oauth/authorize")
    fun authorize(
            @Header(HEADER_OAUTH_TOKEN) token : String,
            @Header(HEADER_OAUTH_SECRET) secret: String
    ): Flowable<String>

    @POST("oauth/access_token")
    fun accessToken(
            @Header(HEADER_OAUTH_TOKEN) token : String,
            @Header(HEADER_OAUTH_SECRET) secret: String,
            @Field("oauth_verifier") pinCode: String
    ): Flowable<String>

    @POST("1.1/statuses/home_timeline.json")
    fun homeTimeline(
            @Body request: TimelineRequest
    ): Flowable<String>
}