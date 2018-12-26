package com.dbscarlet.common.util

import android.util.Log

/**
 * Created by Daibing Wang on 2018/6/21.
 */
object LogUtil {
    var debug: Boolean = true//是否打印log
    var defaultTag = "LogUtil"
    private val LINE = "════════════════════════════════════════════════════════════"
    private val MSG_LENGTH_LIMIT = 3 * 1024
    private val logI : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.i(t, m) else Log.i(t, m, tr)}
    private val logW : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.w(t, m) else Log.w(t, m, tr)}
    private val logE : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.e(t, m) else Log.e(t, m, tr)}
    private val logD : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.d(t, m) else Log.d(t, m, tr)}
    private val logV : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.v(t, m) else Log.v(t, m, tr)}
    private val logA : (String, String, Throwable?) -> Unit =
            { t, m, tr -> if (tr == null) Log.wtf(t, m) else Log.wtf(t, m, tr)}

    internal fun printLog(tag: String, msg: String, throwable: Throwable?, level: Int) {
        if (!debug) {
            return
        }
        val printer = when (level) {
            Log.INFO-> logI
            Log.WARN-> logW
            Log.ERROR-> logE
            Log.DEBUG-> logD
            else -> logV
        }
        val targetStack = targetStackTraceMSg()
        val logStr = "$LINE\n$targetStack$msg"
        var start = 0
        val len = logStr.length
        while (start + MSG_LENGTH_LIMIT < len) {
            printer.invoke(tag, logStr.substring(start, start + MSG_LENGTH_LIMIT), null)
            start += MSG_LENGTH_LIMIT
        }
        printer.invoke(tag, "${logStr.substring(start)}\n$LINE\n", throwable)
    }


    private fun targetStackTraceMSg(): String {
        val targetStackTraceElement = getTargetStackTraceElement()
        return if (targetStackTraceElement != null) {
            "Log at ${targetStackTraceElement.className}.${targetStackTraceElement.methodName}(${targetStackTraceElement.fileName}:${targetStackTraceElement.lineNumber})\n"
        } else {
            ""
        }
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        val stackTrace = Thread.currentThread().stackTrace
        var methodLevel = 3
        for (stackTraceElement in stackTrace) {
            val inLogUtil = stackTraceElement.className.contains(LogUtil::class.java.name)
            if (!inLogUtil) {
                methodLevel--
            }
            Log.i("stackTrace", stackTraceElement.className)
            if (methodLevel == 0) {
                targetStackTrace = stackTraceElement
                break
            }
        }
        return targetStackTrace
    }
}

fun logI(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.INFO)
}
fun logW(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.WARN)
}
fun logE(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.ERROR)
}
fun logD(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.DEBUG)
}
fun logV(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.VERBOSE)
}