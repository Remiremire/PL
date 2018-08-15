package com.dbscarlet.common.interfaces

/**
 * Created by Daibing Wang on 2018/8/15.
 */
interface Converter<T, R> {
    fun convert(original: T): R
}