package com.dbscarlet.pl.main.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.applib.twitterNetwork.toFormDataLiveData
import com.dbscarlet.applib.twitterNetwork.toStringLiveData
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.webService.AuthorizeWebService
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/14.
 */
class AuthorizeRepository
    @Inject
    constructor(
            private val webService: AuthorizeWebService
    ) {

    fun requestToken(): LiveData<Resource<Map<String, String>>> {
        return webService.requestToken()
                .toFormDataLiveData()
    }

    fun getLoginHtml(token: String, secret: String): LiveData<Resource<String>> {
        return webService.authorize(token, secret)
                .toStringLiveData()
    }

    fun accessToken(pinCode: String, token: String, secret: String): LiveData<Resource<Map<String, String>>> {
        return webService.accessToken(pinCode, token, secret)
                .toFormDataLiveData()
    }
}