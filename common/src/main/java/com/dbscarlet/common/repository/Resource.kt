package com.dbscarlet.common.repository

/**
 * Created by Daibing Wang on 2018/7/16.
 */
class Resource<T> private constructor(
        var state: State,
        var code: Int? = null,
        var message: String? = null,
        var data: T? = null,
        var progress: Double? = null,
        var total: Double? = null,
        var cause: Exception? = null
) {
    companion object {
        fun <T> loading(message: String?, progress: Double?, total: Double?): Resource<T> {
            return Resource(State.LOADING, progress = progress, total = total)
        }

        fun <T> success(data: T): Resource<T> {
            return Resource(State.SUCCESS, data = data)
        }

        fun <T> fail(code: Int?, message: String?): Resource<T> {
            return Resource(State.FAILED, code = code, message = message)
        }

        fun <T> exception(cause: Exception?): Resource<T> {
            return Resource(State.EXCEPTION, cause = cause)
        }
    }

}