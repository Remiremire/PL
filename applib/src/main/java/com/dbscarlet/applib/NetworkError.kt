package com.dbscarlet.applib

/**
 * Created by Daibing Wang on 2018/8/16.
 */
enum class NetworkError constructor(val code: Int, val msg: String) {
    UNKNOWN(-1, "未知错误"),
    NO_RESPONSE(-2, "服务无响应"),
    NO_RESPONSE_BODY(-3, "服务响应异常"),
    UNKNOWN_SERVICE_ERROR(-4, "未知的服务异常"),
    PARSE_ERROR(-5, "服务响应错误")
}
