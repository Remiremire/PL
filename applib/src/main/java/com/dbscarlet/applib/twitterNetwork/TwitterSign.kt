package com.dbscarlet.applib.twitterNetwork

import android.util.Base64
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * Created by Daibing Wang on 2018/7/18.
 */

class TwitterSignInterceptor: Interceptor {
    private val TAG = TwitterSignInterceptor::class.simpleName
    private val HMAC_SHA1_ALGORITHM = "HmacSHA1"
    private val UTF8 = Charset.forName("UTF-8")
    private val random = Random()
    private val randomStrFilterRegex = Regex("[^0-9a-zA-Z]")

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(signRequest(chain.request()))
    }

    /**
     * 对请求添加基础参数并签名
     */
    private fun signRequest(request: Request): Request {
        val token: String? = request.header(HEADER_OAUTH_TOKEN)
        val secret: String? = request.header(HEADER_OAUTH_SECRET)

        val finalToken = if (token != null && secret != null) token else OAUTH_TOKEN ?: DEF_OAUTH_TOKEN
        val finalSecret = if (token != null && secret != null) secret else OAUTH_TOKEN_SECRET ?: DEF_OAUTH_TOKEN_SECRET

        val paramList = mutableListOf<String>()
        //原始参数
        val url = request.url()
        url.query()?.split('&')?.forEach{ paramList.add(it) }
        getBodyString(request)?.split('&')?.forEach{ paramList.add(it) }

        //添加基础参数
        val baseParams = createBaseParams(finalToken)
        baseParams.forEach {entry -> paramList.add("${entry.key}=${entry.value}")}

        //参数排序，然后组成一个字符串
        paramList.sort()
        val paramStringBuilder = StringBuilder()
        paramList.forEach { paramStringBuilder.append("$it&") }
        paramStringBuilder.replace(paramStringBuilder.length - 1, paramStringBuilder.length, "")
        //用请求方法、url、参数，构成被签名text
        val encodeUrl = URLEncoder.encode(url.toString(), "UTF-8")
        val signText = "${request.method().toUpperCase()}&$encodeUrl&${URLEncoder.encode(paramStringBuilder.toString(), "UTF-8")}"
        //生成签名oauth_signature
        //签名key
        val signKey = "$CONSUMER_SECRET&$finalSecret"
        val signature = encodeHMAC(signText, signKey)

        //用原始url和基础参数、签名生成新url
        return request.newBuilder()
                .removeHeader(HEADER_OAUTH_TOKEN)
                .removeHeader(HEADER_OAUTH_SECRET)
                .addHeader("Authorization", createAuthorizationBody(baseParams, signature))
                .build()

    }

    /**
     * 创建授权header
     */
    private fun createAuthorizationBody(baseParams: Map<String, String>, signature: String): String{
        val builder = StringBuilder("OAuth ")
        val utf8 = "UTF-8"
        baseParams.forEach{
            entry-> builder.append("${entry.key}=\"${URLEncoder.encode(entry.value, utf8)}\", ")
        }
        builder.append("oauth_signature=\"${URLEncoder.encode(signature, utf8)}\"")
        return builder.toString()
    }

    /**
     * 生成基础参数
     */
    private fun createBaseParams(authToken: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["oauth_consumer_key"] = CONSUMER_KEY
        map["oauth_signature_method"] = SIGNATURE_METHOD
        map["oauth_version"] = OAUTH_VERSION
        map["oauth_token"] = authToken
        val randomString = randomString()
        val timeStamp = System.currentTimeMillis() / 1000
        map["oauth_nonce"] = randomString
        map["oauth_timestamp"] = timeStamp.toString()
        return map
    }

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data
     * 被签名的字符串
     * @param key
     * 密钥
     * @return
     * 加密后的字符串
     */
    fun encodeHMAC(data: String, key: String): String {
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            val signinKey = SecretKeySpec(key.toByteArray(), HMAC_SHA1_ALGORITHM)
            //生成一个指定 Mac 算法 的 Mac 对象
            val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey)
            //完成 Mac 操作
            val rawHmac = mac.doFinal(data.toByteArray())
            return String(Base64.encode(rawHmac, Base64.DEFAULT), Charset.forName("UTF-8"))
        } catch (e: NoSuchAlgorithmException) {
            System.err.println(e.message)
        } catch (e: InvalidKeyException) {
            System.err.println(e.message)
        }
        throw IllegalArgumentException("network HMAC-SHA1 encode Error")
    }

    private fun randomString(): String {
        val md5 = MessageDigest.getInstance("md5")
        val digest = md5.digest("${System.currentTimeMillis()}${random.nextLong()}}".toByteArray())
        return String(Base64.encode(digest, Base64.DEFAULT), Charset.forName("UTF-8"))
                .replace(randomStrFilterRegex, "")
    }

    /**
     * 获取请求body，(比如post参数)
     */
    private fun getBodyString(request: Request): String? {
        try {
            val copy = request.newBuilder().build()
            val body = copy.body() ?: return null
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = getCharset(body.contentType())
            return buffer.readString(charset)
        } catch (e: Exception) {
            Log.e(TAG, "getBodyString Error", e)
        }
        return null
    }

    private fun getCharset(contentType: MediaType?): Charset {
        val charset: Charset? = if (contentType != null) contentType.charset(UTF8) else UTF8
        return charset ?: UTF8
    }
}