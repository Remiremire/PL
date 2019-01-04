package com.dbscarlet.applib.networkConfig

import com.dbscarlet.applib.networkConfig.netConverter.NetConverterFactory
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Daibing Wang on 2017/4/18.
 */

object Network {

    lateinit var server: Api

    private val TAG = "Network"

    private val BASE_URL = "BaseUrl"


    fun initNetwork() {
        val okHttpClient = OkHttpClient.Builder()
                //打印日志
//                 .addInterceptor(NetLogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        server = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(NetConverterFactory(Gson()))
                .build()
                .create(Api::class.java)

    }

}
