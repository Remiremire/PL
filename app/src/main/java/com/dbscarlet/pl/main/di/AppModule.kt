package com.dbscarlet.pl.main.di

import com.dbscarlet.applib.contact.TwitterUrl
import com.dbscarlet.applib.twitterNetwork.TwitterSignInterceptor
import com.dbscarlet.common.di.CommonAppModule
import com.dbscarlet.pl.main.application.App
import com.dbscarlet.pl.main.webService.TwitterApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by Daibing Wang on 2018/9/17.
 */
@Module
class AppModule: CommonAppModule<App> {

    val twitterApi: TwitterApi
    @Provides get

    constructor(app: App) : super(app) {
        val httpClient = OkHttpClient.Builder()
                .writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(TwitterSignInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(TwitterUrl.HOST)
                .client(httpClient)
                .build()
        twitterApi = retrofit
                .create(TwitterApi::class.java)
    }
}