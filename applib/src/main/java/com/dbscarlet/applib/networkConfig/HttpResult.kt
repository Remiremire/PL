package com.dbscarlet.applib.networkConfig

/**
 * Created by Daibing Wang on 2018/10/11.
 * 如果字段名称改变，需要同步修改[NetConverterFactory]中的代码
 */
class HttpResult<T> {
    var data: T? = null
    var status: String? = null
    var msg: String? = null

}
