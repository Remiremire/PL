package com.dbscarlet.common.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Created by Daibing Wang on 2018/8/14.
 */
private val uiThreadHandler = Handler(Looper.getMainLooper())

fun isUiThread(thread: Thread = Thread.currentThread()): Boolean {
    return thread.name == Looper.getMainLooper().thread.name
}

fun runOnUiThread(delay: Long = 0, runnable: ()-> Unit) {
    if (delay <= 0) {
        uiThreadHandler.post(runnable)
    } else {
        uiThreadHandler.postDelayed(runnable, delay)
    }
}

fun toastShort(text: String, context: Context = AppInfo.app) {
    if (isUiThread()) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    } else {
        runOnUiThread { Toast.makeText(context, text, Toast.LENGTH_SHORT).show() }
    }
}

fun toastLong(text: String, context: Context = AppInfo.app) {
    if (isUiThread()) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    } else {
        runOnUiThread { Toast.makeText(context, text, Toast.LENGTH_LONG).show() }
    }
}