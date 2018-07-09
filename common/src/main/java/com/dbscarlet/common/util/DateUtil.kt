package com.dbscarlet.common.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Daibing Wang on 2018/4/27.
 */
fun Long.toFormateDate(regulation: String) : String {
    return SimpleDateFormat(regulation)
            .format(Date(this))
}

fun String.toTimestamp(regulation: String) : Long {
    return SimpleDateFormat(regulation)
            .parse(this)
            .time
}
