package com.dbscarlet.applib.twitterNetwork

import com.dbscarlet.applib.NetworkError

/**
 * Created by Daibing Wang on 2018/8/16.
 */
abstract class FormDataCallback: BaseCallback<Map<String, String>>() {
    override fun convertString(string: String?): Map<String, String> {
        val dataMap = mutableMapOf<String, String>()
        if (string == null || string.isEmpty()) {
            return dataMap
        }
        val parseException = TwitterApiException(NetworkError.PARSE_ERROR)
        string.split("&")
                .forEach{
                    try {
                        val formData = it.split("=")
                        val key = formData[0]
                        when(formData.size) {
                            1-> dataMap[key] = ""
                            2-> dataMap[key] = formData[1]
                            else-> throw parseException
                        }
                    } catch (e: Exception) {
                        throw parseException
                    }
                }
        return dataMap
    }

}