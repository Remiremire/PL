package com.dbscarlet.applib.twitterNetwork

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class TwitterErrorEntity {
    var errors: List<Error>? = null

    class Error {
        var message: String? = null
        var code: Int? = null
    }
}