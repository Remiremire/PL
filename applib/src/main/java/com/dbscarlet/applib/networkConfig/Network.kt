package com.dbscarlet.applib.networkConfig

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Daibing Wang on 2017/4/18.
 */

object Network {

    lateinit var server: Api

    private val TAG = "Network"

    private val BASE_URL = ""


    fun init() {
        val okHttpClient = OkHttpClient.Builder()
                //打印日志
//                .addInterceptor(LogInterceptor())
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

    /**
     * 拦截器，用于添加公共参数和打印请求相关信息
     */
    private class LogInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val body = request.body()
            val bodyStr = requestBodyToString(body)
            val response = chain.proceed(request)
            val originalBody = response.body()
            var byteCount = originalBody!!.contentLength()
            byteCount = if (byteCount <= 0) 1024 * 1024 else byteCount
            val peekBody = response.peekBody(byteCount)
            //打印请求相关信息
            Log.i(TAG, StringBuffer()
                    .append("request: ")
                    .append("\n\t\turl: ").append(request.url().toString())
                    .append("\n\t\tparams: ").append(bodyStr)
                    .append("\n\t\theader: ").append(request.headers().toString())
                    .append("\nresponse: ")
                    .append("\n\t\tbody: ").append(peekBody.string())
                    .toString())
            return response
        }

        private fun requestBodyToString(body: RequestBody?): String {
            try {
                val buffer = Buffer()
                if (body != null)
                    body.writeTo(buffer)
                else
                    return ""
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "did not work"
            }

        }
    }
}
