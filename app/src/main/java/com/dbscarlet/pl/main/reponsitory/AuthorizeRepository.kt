package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.webService.TwitterApi
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class AuthorizeRepository
    @Inject
    constructor(
            private val twitterService: TwitterApi
    ) {

    fun requestToken(): LiveData<Resource<Map<String, String>>> {
//        return webService.requestToken()
//                .toFormDataLiveData()
        twitterService.requestToken()
                .subscribe()
        TODO()
    }

    fun getLoginHtml(token: String, secret: String): LiveData<Resource<String>> {
//        return webService.authorize(token, secret)
//                .toStringLiveData()
        twitterService.authorize(token, secret)
        TODO()
    }

    fun accessToken(pinCode: String, token: String, secret: String): LiveData<Resource<Map<String, String>>> {
//        return webService.accessToken(pinCode, token, secret)
//                .toFormDataLiveData()
        twitterService.accessToken(token, secret, pinCode)
        TODO()
    }
}