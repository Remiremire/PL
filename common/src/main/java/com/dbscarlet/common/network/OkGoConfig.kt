package com.dbscarlet.common.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.convert.StringConvert
import com.lzy.okgo.request.base.Request
import com.lzy.okrx2.adapter.FlowableResponse
import io.reactivex.FlowableSubscriber

/**
 * Created by Daibing Wang on 2018/5/10.
 */
/**
 * 通过转换器，可以将任意类型对象作为请求参数
 * @param param 请求参数对象拓展
 * @param converter 将param转换为Map<String, String>的函数
 */
fun <T> Request<*,*>.params(param: T, isReplace: Boolean = true, converter:(T) -> Map<String,String>?) : Request<*, *>{
    val paramMap = converter.invoke(param)
    params(paramMap, isReplace)
    return this
}

/**
 * RESTFul Api params, replace "{$key}" by value
 */
fun Request<*, *>.restParams(key: String, value: Any) : Request<*, *>{
    this.url.replace("{$key}", "$value")
    return this
}

fun Request<*, *>.restParams(params: Map<String, Any>) : Request<*, *> {
    params.forEach{ (key, value)-> restParams(key, value) }
    return this
}

fun Request<*, *>.restParams(paramsObj: Any) : Request<*, *> {
    paramsObj::class.java.declaredFields
            .forEach { restParams(it.name, it.get(paramsObj)) }
    return this
}

fun <T, R : Request<String, *>> Request<String, R>.subscribe(subscriber: FlowableSubscriber<T>) {
    converter(StringConvert())
            .adapt(FlowableResponse<String>())
            .map { Gson().fromJson<T>(it.body(), object : TypeToken<T>() {}.type)}
}




