package com.dbscarlet.pl.main.core.authorize

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.dbscarlet.applib.contact.SPSaveKey
import com.dbscarlet.common.basic.BaseVM
import com.dbscarlet.common.dataResource.ResObserver
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.application.App
import com.dbscarlet.pl.main.reponsitory.AuthorizeRepository
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/17.
 */
class AuthorizeVM
    @Inject constructor(
            private val repository: AuthorizeRepository
    ) : BaseVM(){
    private var token: String? = null
    private var secret: String? = null
    @Inject
    lateinit var app: App

    fun getLoginHtml(): LiveData<Resource<String>> {
        val liveData = MutableLiveData<Resource<String>>()
        liveData.value = Resource.loading("加载中...")
        val token = this.token
        val secret = this.secret
        if (token == null || secret == null) {
            requestToken(liveData)
        } else {
            authorize(token, secret, liveData)
        }
        return liveData
    }

    private fun authorize(token: String, secret: String, liveData: MutableLiveData<Resource<String>>) {
        repository.getLoginHtml(token, secret)
                .observeForever(object : ResObserver<String>(){
                    override fun onSuccess(res: Resource<String>, data: String) {
                        liveData.value = Resource.success(data)
                    }

                    override fun onFailed(res: Resource<String>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.failed(code, msg, cause)
                    }

                    override fun onException(res: Resource<String>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.exception(code, msg, cause)
                    }
                })
    }

    private fun requestToken(liveData: MutableLiveData<Resource<String>>) {
        repository.requestToken()
                .observeForever(object : ResObserver<Map<String, String>>() {
                    override fun onSuccess(res: Resource<Map<String, String>>, data: Map<String, String>) {
                        val token = data["oauth_token"]
                        val secret = data["oauth_token_secret"]

                        if (token == null || secret == null) {
                            liveData.value = Resource.failed(msg = "服务无响应")
                        } else {
                            this@AuthorizeVM.token = token
                            this@AuthorizeVM.secret = secret
                            authorize(token, secret, liveData)
                        }
                    }

                    override fun onFailed(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.failed(code, msg, cause)
                    }

                    override fun onException(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.exception(code, msg, cause)
                    }
                })
    }

    fun login(pinCode: String) : LiveData<Resource<Unit>> {
        val liveData = MutableLiveData<Resource<Unit>>()
        val token = this.token
        val secret = this.secret
        if (token == null || secret == null) {
            liveData.value = Resource.failed(msg = "请求已过期")
            return liveData
        }
        liveData.value = Resource.loading("加载中...")
        repository.accessToken(pinCode, token, secret)
                .observeForever(object : ResObserver<Map<String, String>>(){
                    override fun onSuccess(res: Resource<Map<String, String>>, data: Map<String, String>) {
                        val oauthToken = data["oauth_token"]
                        val oauthTokenSecret = data["oauth_token_secret"]
                        if (oauthToken == null || oauthTokenSecret == null ||
                                oauthToken.isNullOrEmpty() || oauthTokenSecret.isNullOrEmpty()) {
                            liveData.value = Resource.failed(msg = "授权失败")
                        } else {
                            val sp = app.getSharedPreferences(SPSaveKey.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                            sp.edit().putString(SPSaveKey.OAUTH_TOKEN, oauthToken)
                                    .putString(SPSaveKey.OAUTH_TOKEN_SECRET, oauthTokenSecret)
                                    .apply()
                            liveData.value = Resource.success(Unit)
                        }
                    }

                    override fun onFailed(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.failed(code, msg, cause)
                    }

                    override fun onException(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        liveData.value = Resource.exception(code, msg, cause)
                    }
                })
        return liveData

    }

}