package com.dbscarlet.common.network

import com.lzy.okgo.convert.Converter
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.lzy.okrx2.adapter.FlowableResponse
import io.reactivex.Flowable

/**
 * Created by Daibing Wang on 2018/5/10.
 */

/**
 * 通过转换器，可以将任意类型对象作为请求参数
 * @params params 请求参数对象拓展
 * @params converter 将param转换为Map<String, String>的函数
 */
fun <R, P, T: Request<R, T>> T.params(params: P, isReplace: Boolean = true, converter:(P) -> Map<String,String>?): T {
    val paramMap = converter.invoke(params)
    params(paramMap, isReplace)
    return this
}

/**
 * 将任意对象转换为Map<String, String>作为请求参数
 * 转换规则：该对象每个非空属性作为一条参数，属性名作为参数名，属性值toString()作为参数值
 */
fun <R, T: Request<R, T>> T.params(params: Any, isReplace: Boolean = true) : T {
    return params(params, isReplace) {
        val fieldMap = mutableMapOf<String, String>()
        params::class.java.declaredFields.forEach {
            if (it.get(params) != null) {
                fieldMap[it.name] = it.get(params).toString()
            }
        }
        fieldMap
    }
}

/**
 * RESTFul Api 参数, 将url中的 "{$key}" 替换为 value
 */
fun <R, T: Request<R, T>> T.restParams(key: String, value: Any): T {
    this.url.replace("{$key}", "$value")
    return this
}

fun <R, T: Request<R, T>> T.restParams(params: Map<String, Any>) : T {
    params.forEach{ (key, value)-> restParams(key, value) }
    return this
}

fun <R, T: Request<R, T>> T.restParams(paramsObj: Any) : T {
    paramsObj::class.java.declaredFields
            .forEach { restParams(it.name, it.get(paramsObj)) }
    return this
}

fun <R, T: Request<R, T>> T.toFlowable(converter: Converter<R>): Flowable<Response<R>> {
    return converter(converter)
            .adapt(FlowableResponse<R>())
}



