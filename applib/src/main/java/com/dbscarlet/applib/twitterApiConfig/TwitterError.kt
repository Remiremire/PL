package com.dbscarlet.applib.twitterApiConfig

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class TwitterError {
    var errors: List<Error>? = null

    class Error {
        var message: String? = null
        var code: Int? = null
    }
}