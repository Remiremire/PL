package com.dbscarlet.applib.twitterNetwork

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.dataResource.Resource
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

/**
 * Created by Daibing Wang on 2018/8/17.
 */
fun <T> Request<T, *>.toJsonLiveData(): LiveData<Resource<T>> {
    val request: Request<T, *> = this
    return object: LiveData<Resource<T>>() {
        override fun onActive() {
            request.tag(request)
            request.execute(object: JsonCallback<T>(){
                override fun onStart(request: Request<T, out Request<Any, Request<*, *>>>?) {
                    super.onStart(request)
                    value = Resource.loading("加载中...")
                }

                override fun onSuccess(response: Response<T>?) {
                    val data = response?.body()
                    value = if (data == null) {
                        Resource.failed(msg = "服务无响应")
                    } else {
                        Resource.success(data)
                    }
                }

                override fun onException(code: Int, msg: String, throwable: Throwable?) {
                    super.onException(code, msg, throwable)
                    value = Resource.exception(code = code, msg = msg, cause = throwable)
                }

                override fun onFailed(code: Int, msg: String, throwable: Throwable?) {
                    super.onFailed(code, msg, throwable)
                    value = Resource.failed(code = code, msg = msg, cause = throwable)
                }

            })
        }

        override fun onInactive() {
            OkGo.cancelTag(OkGo.getInstance().okHttpClient, request)
        }
    }
}

fun Request<String, *>.toStringLiveData(): LiveData<Resource<String>> {
    val request = this
    return object : LiveData<Resource<String>>() {
        override fun onActive() {

        }

        override fun onInactive() {
            OkGo.cancelTag(OkGo.getInstance().okHttpClient, request)
        }
    }
}