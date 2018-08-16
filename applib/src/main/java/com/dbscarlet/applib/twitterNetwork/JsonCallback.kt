package com.dbscarlet.applib.twitterNetwork

import com.dbscarlet.applib.NetworkError
import com.dbscarlet.common.util.gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType

/**
 * Created by Daibing Wang on 2018/8/15.
 */

abstract class JsonCallback<T>: BaseCallback<T>() {
    override fun convertString(string: String?): T {
        val clazz = this::class.java
        var supperClazz = clazz.superclass
        while (supperClazz != JsonCallback::class.java) {
            supperClazz = supperClazz.superclass
        }
        val dataType = (supperClazz.genericSuperclass as ParameterizedType).actualTypeArguments[0]

        @Suppress("UNCHECKED_CAST")
        if (dataType == Void.TYPE || dataType == Unit::class.java) {
            return gson.fromJson("{}", Void::class.java) as T
        }
        try {
            return gson.fromJson<T>(string, object :TypeToken<T>(){}.type)
        } catch (e: Exception) {
            throw TwitterApiException(NetworkError.PARSE_ERROR, "网络响应异常")
        }
    }
}