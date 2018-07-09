package com.dbscarlet.common.util

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.widget.TextView

/**
 * Created by Daibing Wang on 2018/6/20.
 */

/**
 * 设置有触发间隔的点击监听
 * @param interval 触发间隔，毫秒
 */
fun View.setOnClickListener(interval: Long,  onClickListener: View.OnClickListener) {
    var lastClickTime : Long = 0
    setOnClickListener{
        val time = System.currentTimeMillis()
        if (time - lastClickTime >= interval) {
            lastClickTime = time
            onClickListener.onClick(this)
        }
    }
}

/**
 * 添加字符到末尾，可独立设置字体大小和颜色
 * @param size  字体大小，单位SP
 * @param color 字体颜色
 */
fun <T: TextView> T.appendWith(text: CharSequence?, size: Int? = null, color: Int? = null) {
    if (text == null || (size == null && color == null)) {
        append(text)
        return
    }
    val sp = SpannableString(text)
    if (size != null) {
        val sizePixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size.toFloat(), resources.displayMetrics).toInt()
        sp.setSpan(AbsoluteSizeSpan(sizePixel), 0, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (color != null) {
        sp.setSpan(ForegroundColorSpan(color), 0, sp.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    append(sp)
}

