package com.dbscarlet.pl.main.core.authorize

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.dbscarlet.common.basic.BaseVM
import com.dbscarlet.common.dataResource.ResObserver
import com.dbscarlet.common.dataResource.Resource
import com.dbscarlet.pl.main.reponsitory.AuthorizeRepository
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/17.
 */
class AuthorizeVM
    @Inject constructor() : BaseVM(){
    @Inject
    lateinit var repository: AuthorizeRepository
    var token: String? = null
    var secret: String? = null

    fun getLoginHtml(): LiveData<Resource<String>> {
        val resultLiveData = MutableLiveData<Resource<String>>()
        resultLiveData.value = Resource.loading("加载中...")
        repository.requestToken()
                .observeForever(object : ResObserver<Map<String, String>>(){
                    override fun onSuccess(res: Resource<Map<String, String>>, data: Map<String, String>) {
                        token = data["oauth_token"]
                        secret = data["oauth_token_secret"]
                        if (token == null || secret == null) {
                            resultLiveData.value = Resource.failed(msg = "服务无响应")
                        }
                    }

                    override fun onFailed(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        resultLiveData.value = Resource.failed(code, msg, cause)
                    }

                    override fun onException(res: Resource<Map<String, String>>, code: Int?, msg: String?, cause: Throwable?) {
                        resultLiveData.value = Resource.exception(code, msg, cause)
                    }
                })
        return resultLiveData
    }
}