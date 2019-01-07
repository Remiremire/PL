package com.dbscarlet.common.commonUtil

import android.view.View

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