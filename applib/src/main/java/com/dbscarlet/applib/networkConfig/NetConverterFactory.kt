package com.dbscarlet.applib.networkConfig

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by Daibing Wang on 2018/10/11.
 */
class NetConverterFactory(private val gson: Gson) : Converter.Factory() {
    companion object {
        private val DEBUG = true
    }
    private val TAG = javaClass.simpleName
    private val jsonParser: JsonParser = JsonParser()

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        if (type == null || type !is Class<*>) {
            return null
        }
        if (RequestBody::class.java.isAssignableFrom(type)) {
            return null
        }
        if (MultipartBody.Part::class.java.isAssignableFrom(type)) {
            return null
        }
        val apiUrlAndMethod = getApiUrlAndMethod(retrofit, methodAnnotations)
        return FieldsFormRequestConverter(apiUrlAndMethod)
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if (type == null) return null
        val apiUrlAndMethod = getApiUrlAndMethod(retrofit, annotations)
        if (type is Class<*> && String::class.java.isAssignableFrom(type)) {
            return StringResponseConverter(apiUrlAndMethod)
        }
        return GsonResponseConverter(apiUrlAndMethod, type, gson.getAdapter(TypeToken.get(type)))
    }

    private inner class FieldsFormRequestConverter(private val apiUrlAndMethod: String) : Converter<Any, RequestBody> {

        @Throws(IOException::class)
        override fun convert(value: Any): RequestBody {
            val jsonStr = gson.toJson(value)
            logNoLimit("$apiUrlAndMethod Request:\n$jsonStr")

            val builder = FormBody.Builder()
            val fields = value.javaClass.declaredFields
            for (f in fields) {
                try {
                    f.isAccessible = true
                    val fieldValue = f.get(value)
                    if (fieldValue != null) {
                        builder.add(f.name, fieldValue.toString())
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
            return builder.build()
        }
    }

    private inner class StringResponseConverter(private val apiUrlAndMethod: String) : Converter<ResponseBody, String> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            value.use {
                val responseStr = it.string()
                logNoLimit("$apiUrlAndMethod Response:\n$responseStr")
                if (responseStr == null) {
                    throw NetException("网络无响应", false, null)
                }
                return responseStr
            }
        }
    }

    private open inner class GsonResponseConverter(
            protected var apiUrlAndMethod: String,
            protected var type: Type,
            protected val adapter: TypeAdapter<*>
    ) : Converter<ResponseBody, Any> {

        /**
         * 对请求结果为HttpResult格式的情况作特殊处理
         * 比如：请求时用户在别处进行了登录，就会返回{status: 901, data:{}, msg: "xxxxx"}
         * 但是可能预期的data应该是数组[]，
         * 此时如果直接解析会失败，因此先只解析status和msg，如果status为200(成功)，才解析data
         */
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Any {
            value.use {
                val jsonStr = getJsonStr(it) ?: throw NetException("网络无响应", false, null)
                val typeStr = type.toString()
                if (TextUtils.isEmpty(typeStr) || !typeStr.startsWith(HttpResult::class.java.name + "<")) {
                    return adapter.fromJson(jsonStr)
                }
                try {
                    val jsonObj = jsonParser.parse(jsonStr).asJsonObject
                    if (jsonObj.get("status").asString == ResponseStatus.SUCCESS) {
                        if (typeStr.startsWith(HttpResult::class.java.name + "<" + List::class.java.name + "<")) {
                            //预期data为集合List,但当没有数据时，服务响应的data可能为空字典{},将其替换为空集合[]
                            val data = jsonObj.get("data")
                            if (data != null && !data.isJsonArray) {
                                jsonObj.remove("data")
                                jsonObj.add("data", JsonArray())
                                Log.e(TAG, "\"data\" in response is jsonObject when it is expected jsonArray")
                            }
                        }
                    } else {
                        //status不是200时(请求失败)，不解析data
                        jsonObj.remove("data")
                    }
                    return adapter.fromJson(jsonObj.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw NetException("数据格式错误", false, e)
                }
            }
        }

        @Throws(IOException::class)
        protected open fun getJsonStr(value: ResponseBody): String? {
            val valueStr = value.string()
            logNoLimit("$apiUrlAndMethod Response:\n$valueStr")
            return valueStr
        }

    }

    private fun getApiUrlAndMethod(retrofit: Retrofit?, methodAnnotations: Array<Annotation>?): String {
        var method = "unknown"
        var url = ""
        var canFindMethod = false
        if (methodAnnotations != null) {
            findMethod@ for (anno in methodAnnotations) {
                when(anno) {
                    is GET -> {
                        method = "GET"
                        url = anno.value
                    }
                    is POST -> {
                        method = "POST"
                        url = anno.value
                    }
                    is PATCH -> {
                        method = "PATCH"
                        url = anno.value
                    }
                    is PUT -> {
                        method = "PUT"
                        url = anno.value
                    }
                    is OPTIONS -> {
                        method = "OPTIONS"
                        url = anno.value
                    }
                    is DELETE -> {
                        method = "DELETE"
                        url = anno.value
                    }
                    is HTTP -> {
                        method = anno.method.toUpperCase()
                        url = anno.path
                    }
                    else -> continue@findMethod
                }
                canFindMethod = true
                break@findMethod
            }
        }

        if (canFindMethod && retrofit != null &&
                !url.startsWith("https://") && !url.startsWith("http://")) {
            url = retrofit.baseUrl().toString() + url
        }
        return String.format("%s <%s>", url, method)
    }

    private fun logNoLimit(info: String) {
        if (!DEBUG) return

        val lengthLimit = 3200
        if (info.length <= lengthLimit) {
            Log.i("Network>>>", info)
        } else {
            Log.i("Network>>>", info.substring(0, lengthLimit))
            logNoLimit("====Continue==>" + info.substring(lengthLimit))
        }
    }
}
