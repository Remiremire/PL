package com.dbscarlet.pl.main.network

import com.dbscarlet.applib.networkConfig.netConverter.NetConverterFactory
import com.dbscarlet.applib.twitterNetwork.TwitterSignInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Daibing Wang on 2017/4/18.
 */

object Network {

    private val TAG = "Network"

    fun initNetwork() {
        val twitterClient = OkHttpClient.Builder()
                .addInterceptor(TwitterSignInterceptor())
                //打印日志
//                .addInterceptor(NetLogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .client(twitterClient)
                .baseUrl("NEED BASE URL")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(NetConverterFactory(Gson()))
                .build()

    }
}
