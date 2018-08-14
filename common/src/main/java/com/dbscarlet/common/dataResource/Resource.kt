package com.dbscarlet.common.dataResource

/**
 * Created by Daibing Wang on 2018/7/16.
 */
class Resource<T>(
        var state: State,
        var code: Int? = null,
        var msg: String? = null,
        var data: T? = null,
        var progress: Double? = null,
        var total: Double? = null,
        var cause: Exception? = null
)