package com.dbscarlet.applib.networkConfig


import java.io.IOException

/**
 * Created by Daibing Wang on 2018/10/11.
 */
class NetException constructor(message: String,
                   cause: Throwable?,
                   val isSystemError: Boolean = false) : IOException(message, cause) {
    override val message: String
        get() = super.message!!
}
