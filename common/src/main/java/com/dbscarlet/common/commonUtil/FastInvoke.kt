package com.dbscarlet.common.commonUtil

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
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

fun Activity.snackbar(msg: String, bgColor: Int? = null, snackbarSet: (Snackbar.()-> Unit)? = null) {
    val snackbar = Snackbar.make(window.decorView, msg, 3000)
    if (bgColor != null) snackbar.view.setBackgroundColor(bgColor)
    snackbarSet?.invoke(snackbar)
    snackbar.show()
}