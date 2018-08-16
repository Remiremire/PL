package com.dbscarlet.applib.contact

/**
 * Created by Daibing Wang on 2018/8/15.
 */
class SPSaveKey private constructor() {
    companion object {
        const val SHARED_PREFERENCES_NAME = "dbscarlet_pl"
        const val OAUTH_TOKEN = "oauth_token"
        const val OAUTH_TOKEN_SECRET = "oauth_secret"
    }
}