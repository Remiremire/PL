package com.dbscarlet.applib.twitterNetwork

import com.dbscarlet.applib.NetworkError
import com.dbscarlet.common.util.gson
import com.dbscarlet.common.util.logE
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.model.Response

/**
 * Created by Daibing Wang on 2018/8/15.
 */
abstract class BaseCallback<T>: AbsCallback<T>() {

    final override fun onError(response: Response<T>?) {
        when {
            response == null -> onFailed(NetworkError.NO_RESPONSE, null)
            response.exception is TwitterApiException -> {
                val twException = response.exception as TwitterApiException
                onFailed(twException.errorCode, twException.errorMsg, twException)
            }
            else -> {
                when(response.code()) {
                    in 500..599 -> onFailed(response.code(), "服务器繁忙", response.exception)
                    in 400..499 -> onFailed(response.code(), "连接超时", response.exception)
                    else -> onException(NetworkError.UNKNOWN, response.exception)
                }
            }
        }
    }

    override fun convertResponse(response: okhttp3.Response?): T {
        val body = response?.body() ?: throw TwitterApiException(NetworkError.NO_RESPONSE_BODY)
        val string = body.string()
        if (string != null && string.isNotEmpty()) {
            val errorEntity = try {
                gson.fromJson<TwitterErrorEntity>(string, TwitterErrorEntity::class.java)
            } catch (e: Exception) {
                null
            }
            val errorList = errorEntity?.errors
            if (errorList != null && errorList.isNotEmpty()) {
                val firstError = errorList[0]
                val code = firstError.code
                val message = firstError.message
                if (code == null || message == null || message.isEmpty()) {
                    throw TwitterApiException(NetworkError.UNKNOWN_SERVICE_ERROR)
                }
                throw TwitterApiException(code, message)
            }
        }
        return convertString(string)
    }

    @Throws(exceptionClasses = [TwitterApiException::class])
    abstract fun convertString(string: String?): T

    private fun onFailed(error: NetworkError, throwable: Throwable?) {
        onFailed(error.code, error.msg, throwable)
    }

    open fun onFailed(code: Int, msg: String, throwable: Throwable?) {
        logE(msg, "TwitterApi", throwable)
    }

    private fun onException(error: NetworkError, throwable: Throwable?) {
        onException(error.code, error.msg, throwable)
    }

    open fun onException(code: Int, msg: String, throwable: Throwable?) {
        logE(msg, "TwitterApi", throwable)
    }
}