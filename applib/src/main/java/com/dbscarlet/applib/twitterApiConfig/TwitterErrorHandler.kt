package com.dbscarlet.applib.twitterApiConfig

import com.google.gson.Gson

/**
 * Created by Daibing Wang on 2018/8/15.
 */
object TwitterErrorHandler {
    val gson = Gson()
    fun parseError(responseText: String?): TwitterError? {
        if (responseText == null) return null
        return gson.fromJson<TwitterError>(responseText, TwitterError::class.java)
    }
}