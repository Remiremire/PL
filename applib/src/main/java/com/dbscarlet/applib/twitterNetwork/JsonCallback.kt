package com.dbscarlet.applib.twitterNetwork

import com.dbscarlet.applib.NetworkError
import com.dbscarlet.common.util.logI

/**
 * Created by Daibing Wang on 2018/8/15.
 */

abstract class JsonCallback<T>: BaseCallback<T>() {
    override fun convertString(string: String?): T {
        logI(string)
        try {
            return parseJson(string)
        } catch (e: Exception) {
            throw TwitterApiException(NetworkError.PARSE_ERROR)
        }
    }

    abstract fun parseJson(jsonStr: String?): T
}