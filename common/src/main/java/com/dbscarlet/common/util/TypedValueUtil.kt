package com.dbscarlet.common.util

import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by Daibing Wang on 2018/12/4.
 */
var displayMetrics: DisplayMetrics? = null

private fun Float.toPx(unit: Int): Int {
    val dm = displayMetrics ?: throw IllegalStateException("displayMetrics can't be null")
    return TypedValue.applyDimension(unit, this, dm).toInt()
}

fun Number.sp2px(): Int {
    return toFloat().toPx(TypedValue.COMPLEX_UNIT_SP)
}

fun Number.dp2px(): Int {
    return toFloat().toPx(TypedValue.COMPLEX_UNIT_DIP)
}

fun Number.px2dp(): Float {
    val dm = displayMetrics ?: throw IllegalStateException("displayMetrics can't be null")
    return this.toFloat() / dm.density
}