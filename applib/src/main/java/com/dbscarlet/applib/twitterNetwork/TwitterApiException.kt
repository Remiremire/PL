package com.dbscarlet.applib.twitterNetwork

import com.dbscarlet.applib.NetworkError

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class TwitterApiException(
        val errorCode: Int,
        val errorMsg: String
): Exception(errorMsg) {
    constructor(error: NetworkError): this(error.code, error.msg)
}