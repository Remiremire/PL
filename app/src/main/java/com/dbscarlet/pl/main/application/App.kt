package com.dbscarlet.pl.main.application

import com.dbscarlet.common.basic.CommonApp
import com.dbscarlet.pl.main.di.DaggerAppComponent
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.security.MessageDigest
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
        val loggingInterceptor = HttpLoggingInterceptor("Network_PL")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        val sslParams = HttpsUtils.getSslSocketFactory()

        val httpClient = OkHttpClient.Builder()
                .writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(ParamResetInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

        val headers = HttpHeaders()
        headers.put("User-Agent", "OhMyBiliBili Android Client/2.1 (100332338@qq.com)")
        val params = HttpParams()
        params.put("appkey", "27eb53fc9058f8c3")

        OkGo.getInstance().init(this)
                .setOkHttpClient(httpClient)
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .addCommonHeaders(headers)
                .addCommonParams(params)
    }

    object ParamResetInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val newReq = originalRequest.newBuilder()
                    .url(signParams(originalRequest.url().toString()))
                    .build()
            return chain.proceed(newReq)
        }

        fun signParams(unSignUrl: String): String {
            if (!unSignUrl.contains('?') || unSignUrl.indexOf('?') >= (unSignUrl.length - 1)) return unSignUrl
            val paramStartIndex = unSignUrl.indexOf('?')
            val url = unSignUrl.substring(0, paramStartIndex)
            val params = unSignUrl.substring(paramStartIndex + 1, unSignUrl.length)
            val paramList = arrayListOf<String>()
            paramList.addAll(params.split('&'))
            paramList.sort()
            var newParams = ""
            for (p in paramList) {
                newParams += "$p&"
            }
            return "$url?$newParams&sign=${string2MD5("${newParams}&sign=27eb53fc9058f8c3")}"
        }

        fun string2MD5(inStr: String): String {
            val md5: MessageDigest?
            try {
                md5 = MessageDigest.getInstance("MD5")
            } catch (e: Exception) {
                println(e.toString())
                e.printStackTrace()
                return ""
            }

            val charArray = inStr.toCharArray()
            val byteArray = ByteArray(charArray.size)
            for (i in charArray.indices)
                byteArray[i] = charArray[i].toByte()
            val md5Bytes = md5.digest(byteArray)
            val hexValue = StringBuffer()
            for (i in md5Bytes.indices) {
                val value = md5Bytes[i].toInt() and 0xff
                if (value < 16)
                    hexValue.append("0")
                hexValue.append(Integer.toHexString(value))
            }
            return hexValue.toString()

        }
    }
}