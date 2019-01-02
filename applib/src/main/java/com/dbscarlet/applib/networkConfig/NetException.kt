package com.dbscarlet.applib.networkConfig

import java.io.IOException

/**
 * Created by Daibing Wang on 2018/10/11.
 */
class NetException(
        message: String,
        val isSystemError: Boolean,
        cause: Throwable?) : IOException(message, cause) {
    override val message: String
        get() = super.message!!
}
