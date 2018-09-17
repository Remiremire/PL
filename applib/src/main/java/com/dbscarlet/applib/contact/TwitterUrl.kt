package com.dbscarlet.applib.contact

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class TwitterUrl {
    companion object {
        const val HOST = "https://api.twitter.com/"

        const val REQUEST_TOKEN = HOST + "oauth/request_token"

        const val AUTHORIZE = HOST + "oauth/authorize"

        const val ACCESS_TOKEN = HOST + "oauth/access_token"

        const val HOME_TIMELINE = HOST + "1.1/statuses/home_timeline.json"
    }
}