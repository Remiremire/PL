package com.dbscarlet.applib

/**
 * Created by Daibing Wang on 2018/6/28.
 */
object ActPath {
    object App {
        private const val BASE = "/app"
        const val HOME = "$BASE/home"
        const val AUTHORIZE = "$BASE/authorize"
    }
    object Test {
        private const val BASE = "/test"
        const val TINKER_TEST = "$BASE/versionInfo"
        const val FIND_PATCH_FILE = "$BASE/findPatchFile"
        const val API_TEST = "$BASE/apiTest"
        const val AUTHENTICATION = "$BASE/authentication"
        const val CURVE_TEST = "$BASE/curveTest"
        const val WIDGET_TEST = "$BASE/widgetTest"
    }
    object Tweet {
        private const val BASE = "/tweets"
        const val TWEET_TEST = "$BASE/tweetTest"
    }
}