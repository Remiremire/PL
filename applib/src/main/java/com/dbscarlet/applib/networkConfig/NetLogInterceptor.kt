package com.dbscarlet.applib.networkConfig

import com.dbscarlet.common.util.logI
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException

private const val TAG = "NetworkLog"
/**
 * Created by Daibing Wang on 2019/1/4.
 * 拦截器，打印请求相关信息
 */
class NetLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val body = request.body()
        val bodyStr = requestBodyToString(body)
        val response = chain.proceed(request)
        val originalBody = response.body()
        var byteCount = originalBody?.contentLength() ?: 0
        byteCount = if (byteCount <= 0) 1024 * 1024 else byteCount
        val peekBody = response.peekBody(byteCount)
        //打印请求相关信息
        val logStr =
                """
                    request
                        url: ${request.url()}
                        body: $bodyStr
                        header: ${request.headers()}
                    response
                        body:  ${peekBody.string()}
                    """.trimIndent()
        logI(logStr, TAG)
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