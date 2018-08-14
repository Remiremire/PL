package com.dbscarlet.common.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Created by Daibing Wang on 2018/8/14.
 */
private val uiThreadHandler = Handler(Looper.getMainLooper())

fun runOnUiThread(delay: Long? = null, runnable: ()-> Unit) {
    if (delay == null || delay == 0L) {
        uiThreadHandler.post(runnable)
    } else {
        uiThreadHandler.postDelayed(runnable, delay)
    }
}

fun toastShort(text: String, context: Context = AppInfo.app) {
    if (Thread.currentThread().name == Looper.getMainLooper().thread.name) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    } else {
        runOnUiThread { Toast.makeText(context, text, Toast.LENGTH_SHORT).show() }
    }
}

fun toastLong(text: String, context: Context = AppInfo.app) {
    if (Thread.currentThread().name == Looper.getMainLooper().thread.name) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    } else {
        runOnUiThread { Toast.makeText(context, text, Toast.LENGTH_LONG).show() }
    }
}