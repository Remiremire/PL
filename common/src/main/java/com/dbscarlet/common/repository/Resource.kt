package com.dbscarlet.common.repository

/**
 * Created by Daibing Wang on 2018/7/16.
 */
class Resource<T> private constructor(
        val state: ResourceState,
        val progress: Double? = null,
        val total: Double? = null,
        val data: T? = null,
        val code: Int? = null,
        val message: String? = null,
        val cause: Exception? = null
) {
    companion object {
        fun <T> loading(message: String?, progress: Double?, total: Double?): Resource<T> {
            return Resource(ResourceState.LOADING, progress = progress, total = total)
        }

        fun <T> success(data: T): Resource<T> {
            return Resource(ResourceState.SUCCESS, data = data)
        }

        fun <T> fail(code: Int?, message: String?): Resource<T> {
            return Resource(ResourceState.FAILED, code = code, message = message)
        }

        fun <T> exception(cause: Exception?): Resource<T> {
            return Resource(ResourceState.EXCEPTION, cause = cause)
        }
    }

}