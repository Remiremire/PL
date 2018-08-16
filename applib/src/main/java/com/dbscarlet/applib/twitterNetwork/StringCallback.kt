package com.dbscarlet.applib.twitterNetwork

/**
 * Created by Daibing Wang on 2018/8/16.
 */
abstract class StringCallback: BaseCallback<String?>() {
    override fun convertString(string: String?): String? {
       return string
    }
}