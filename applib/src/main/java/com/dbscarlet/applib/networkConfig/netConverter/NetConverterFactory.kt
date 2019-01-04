package com.dbscarlet.applib.networkConfig.netConverter


import android.util.Log
import com.dbscarlet.applib.networkConfig.NetException
import com.dbscarlet.applib.networkConfig.annotation.MultipartRequest
import com.google.gson.Gson
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
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Daibing Wang on 2018/10/11.
 */
class NetConverterFactory(private val gson: Gson) : Converter.Factory() {
    companion object {
        private val DEBUG = true
    }

    private val jsonParser: JsonParser = JsonParser()
    private val TAG = javaClass.simpleName

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody>? {
        type ?: return null
        if (type !is Class<*>) {
            return null
        }
        if (RequestBody::class.java.isAssignableFrom(type)) {
            return null
        }
        if (MultipartBody.Part::class.java.isAssignableFrom(type)) {
            return null
        }
        var isMultipart = false
        parameterAnnotations?.forEach {
            if (it is MultipartRequest) {
                isMultipart = true
                return@forEach
            }
        }
        val apiUrlAndMethod = getApiUrlAndMethod(retrofit, methodAnnotations)
        return when {
            isMultipart -> MultipartRequestConverter(apiUrlAndMethod, FormConverter.FIELD_FORM_CONVERTER)
            else -> FormBodyRequestConverter(apiUrlAndMethod, FormConverter.FIELD_FORM_CONVERTER)
        }
    }

    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        type ?: return null
        val apiUrlAndMethod = getApiUrlAndMethod(retrofit, annotations)
        if (type is Class<*> && String::class.java.isAssignableFrom(type)) {
            return StringResponseConverter(apiUrlAndMethod)
        }
        return GsonResponseConverter(apiUrlAndMethod, type, gson.getAdapter(TypeToken.get(type)))
    }

    private inner class FormBodyRequestConverter<T: Any>(private val apiUrlAndMethod: String,
                                                    private val formConverter: FormConverter<T>) : Converter<T, RequestBody> {

        @Throws(IOException::class)
        override fun convert(value: T?): RequestBody {
            value ?: return FormBody.Builder().build()

            val jsonStr = gson.toJson(value)
            logNoLimit("$apiUrlAndMethod Request:\n$jsonStr")

            val builder = FormBody.Builder()
            val formMap = formConverter.objectToFormMap(value)
            val entries = formMap.entries
            for ((key, value1) in entries) {
                builder.add(key, value1)
            }
            return builder.build()
        }
    }

    private inner class MultipartRequestConverter<T: Any>(private val apiUrlAndMethod: String,
                                                     private val formConverter: FormConverter<T>) : Converter<T, RequestBody> {

        @Throws(IOException::class)
        override fun convert(obj: T?): RequestBody {
            obj ?: return MultipartBody.Builder().build()

            val jsonStr = gson.toJson(obj)
            logNoLimit("$apiUrlAndMethod Request:\n$jsonStr")

            val builder = MultipartBody.Builder()
            val fields = obj::class.java.declaredFields
            try {
                for (f in fields) {
                    f.isAccessible = true
                    val value = f.get(obj)
                    if (value == null || f.type.isPrimitive) {
                        continue
                    }
                    if (isPart(f)) {
                        builder.addPart(value as MultipartBody.Part)
                        f.set(obj, null)
                    } else if (isPartCollection(f)) {
                        val parts = value as Collection<MultipartBody.Part>
                        for (p in parts) {
                            builder.addPart(p)
                        }
                        f.set(obj, null)
                    }
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            val formMap = formConverter.objectToFormMap(obj)
            val entries = formMap.entries
            for ((key, value) in entries) {
                builder.addFormDataPart(key, value)
            }
            return builder.build()
        }

        private fun isPart(field: Field): Boolean {
            return field.type == MultipartBody.Part::class.java
        }

        private fun isPartCollection(field: Field): Boolean {
            if (!Collection::class.java.isAssignableFrom(field.type))
                return false
            val genericType = field.genericType as? ParameterizedType ?: return false
            val types = genericType.actualTypeArguments
            return if (types == null || types.isEmpty()) {
                false
            } else types[0] == MultipartBody.Part::class.java
        }
    }

    private inner class StringResponseConverter(private val apiUrlAndMethod: String) : Converter<ResponseBody, String> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            value.use {
                val responseStr = it.string()
                logNoLimit("$apiUrlAndMethod Response:\n$responseStr")
                if (responseStr == null) {
                    throw NetException("网络无响应", null)
                }
                return responseStr
            }
        }
    }

    private open inner class GsonResponseConverter(
            protected val apiUrlAndMethod: String,
            protected val type: Type,
            protected val adapter: TypeAdapter<out Any>) : Converter<ResponseBody, Any> {

        /**
         * 对请求结果为HttpResult格式的情况作特殊处理
         * 比如：请求时用户在别处进行了登录，就会返回{status: 901, data:{}, msg: "xxxxx"}
         * 但是可能预期的data应该是数组[]，
         * 此时如果直接解析会失败，因此先只解析status和msg，如果status为200(成功)，才解析data
         */
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Any {
            value.use {
                val jsonStr = getJsonStr(it) ?: throw NetException("网络无响应", null)
                return adapter.fromJson(jsonStr)
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
        var canfindMethod = true
        if (methodAnnotations != null) {
            findMethod@ for (it in methodAnnotations) {
                when(it) {
                    is GET -> {
                        method = "GET"
                        url = it.value
                    }
                    is POST -> {
                        method = "POST"
                        url = it.value
                    }
                    is PATCH -> {
                        method = "PATCH"
                        url = it.value
                    }
                    is PUT -> {
                        method = "PUT"
                        url = it.value
                    }
                    is OPTIONS -> {
                        method = "OPTIONS"
                        url = it.value
                    }
                    is DELETE -> {
                        method = "DELETE"
                        url = it.value
                    }
                    is HTTP -> {
                        method = it.method.toUpperCase()
                        url = it.path
                    }
                    else -> continue@findMethod
                }
                canfindMethod = true
                break@findMethod
            }
        }
        if (canfindMethod && !url.startsWith("https://") && !url.startsWith("http://")) {
            url = (retrofit?.baseUrl()?.toString() ?: "NO_BASE_URL/") + url
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
