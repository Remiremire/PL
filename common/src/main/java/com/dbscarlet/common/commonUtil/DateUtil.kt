package com.dbscarlet.common.commonUtil

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Daibing Wang on 2018/4/27.
 */

/**
 * 时间戳转[Date]
 */
fun Long.toDate(): Date {
    return Date(this)
}

/**
 * 时间戳按格式[format]转日期字符串
 */
fun Long.toDateString(format: String): String {
    return SimpleDateFormat(format)
            .format(this.toDate())
}

/**
 * 日期字符串转[Date]
 */
fun String.toDate(format: String): Date {
    return SimpleDateFormat(format)
            .parse(this)
}

/**
 * 日期字符串转时间戳
 */
fun String.toTimestamp(format: String): Long {
    return toDate(format).time
}
