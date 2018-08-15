package com.dbscarlet.applib.contact

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class TwitterApiUrl {
    companion object {
        const val HOST = "https://api.twitter.com/"

        const val REQUEST_TOKEN = HOST + "oauth/request_token"

        const val AUTHORIZE = HOST + "oauth/authorize"

        const val ACCESS_TOKEN = HOST + "oauth/access_token"
    }
}