package com.dbscarlet.common.dataResource

/**
 * Created by Daibing Wang on 2018/7/16.
 */
class Resource<T>(
        var state: State,
        var code: Int? = null,
        var msg: String? = null,
        var data: T? = null,
        var progress: Double? = null,
        var total: Double? = null,
        var cause: Throwable? = null
) {
    companion object {
        fun <T> loading(msg: String? = null, progress: Double? = null, total: Double? = null): Resource<T> {
            return Resource(State.LOADING, msg = msg, progress = progress, total = total)
        }

        fun <T> failed(code: Int? = null, msg: String? = null,cause: Throwable? = null): Resource<T> {
            return Resource(State.FAILED, code = code, msg = msg, cause = cause)
        }

        fun <T> exception(code: Int? = null, msg: String? = null, cause: Throwable?): Resource<T> {
            return Resource(State.EXCEPTION, code = code, msg = msg, cause = cause)
        }

        fun <T> success(data: T): Resource<T> {
            return Resource(State.SUCCESS, data = data)
        }
    }
}