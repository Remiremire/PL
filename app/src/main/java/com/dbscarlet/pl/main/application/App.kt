package com.dbscarlet.pl.main.application

import android.content.Context
import com.dbscarlet.applib.contact.SPSaveKey
import com.dbscarlet.applib.twitterNetwork.TwitterSignInterceptor
import com.dbscarlet.applib.twitterNetwork.setToken
import com.dbscarlet.common.basic.CommonApp
import com.dbscarlet.pl.main.di.DaggerAppComponent
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import java.util.logging.Level


/**
 * Created by Daibing Wang on 2018/5/10.
 */
class App : CommonApp() {

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .build()
                .inject(this)
        initNetwork()
    }

    private fun initNetwork() {
        val sp = getSharedPreferences(SPSaveKey.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val oauthToken = sp.getString(SPSaveKey.OAUTH_TOKEN, null)
        val secret = sp.getString(SPSaveKey.OAUTH_TOKEN_SECRET, null)
        if (oauthToken != null && secret != null) {
            setToken(oauthToken, secret)
        }
        val loggingInterceptor = HttpLoggingInterceptor("Network_PL")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        val sslParams = HttpsUtils.getSslSocketFactory()

        val httpClient = OkHttpClient.Builder()
                .writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(TwitterSignInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()

        val headers = HttpHeaders()
        val params = HttpParams()

        OkGo.getInstance().init(this)
                .setOkHttpClient(httpClient)
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(0)
                .addCommonHeaders(headers)
                .addCommonParams(params)
    }

}