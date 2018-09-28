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
    private val logI : (String, String) -> Unit = {t, m -> Log.i(t, m)}
    private val logW : (String, String) -> Unit = {t, m -> Log.w(t, m)}
    private val logE : (String, String) -> Unit = {t, m -> Log.e(t, m)}
    private val logD : (String, String) -> Unit = {t, m -> Log.d(t, m)}
    private val logV : (String, String) -> Unit = {t, m -> Log.v(t, m)}
    private val logA : (String, String) -> Unit = {t, m -> Log.wtf(t, m)}
    private val logTrI : (String, String, Throwable) -> Unit = {t, m, tr -> Log.i(t, m, tr)}
    private val logTrW : (String, String, Throwable) -> Unit = {t, m, tr -> Log.w(t, m, tr)}
    private val logTrE : (String, String, Throwable) -> Unit = {t, m, tr -> Log.e(t, m, tr)}
    private val logTrD : (String, String, Throwable) -> Unit = {t, m, tr -> Log.d(t, m, tr)}
    private val logTrV : (String, String, Throwable) -> Unit = {t, m, tr -> Log.v(t, m, tr)}
    private val logTrA : (String, String, Throwable) -> Unit = {t, m, tr -> Log.wtf(t, m, tr)}

    internal fun printLog(tag: String, msg: String, throwable: Throwable?, level: Int) {
        if (!debug) {
            return
        }
        val printer: (String, String) -> Unit
        val trPrinter: (String, String, Throwable) -> Unit
        when (level) {
            Log.INFO-> {
                printer = logI
                trPrinter = logTrI
            }
            Log.WARN-> {
                printer = logW
                trPrinter = logTrW
            }
            Log.ERROR-> {
                printer = logE
                trPrinter = logTrE
            }
            Log.DEBUG-> {
                printer = logD
                trPrinter = logTrD
            }
            Log.ASSERT-> {
                printer = logA
                trPrinter = logTrA
            }
            else -> {
                printer = logV
                trPrinter = logTrV
            }
        }
        val targetStack = targetStackTraceMSg()
        val logStr = "$LINE\n$targetStack$msg"
        var start = 0
        val len = logStr.length
        while (start + MSG_LENGTH_LIMIT < len) {
            printer.invoke(tag, logStr.substring(start, start + MSG_LENGTH_LIMIT))
            start += MSG_LENGTH_LIMIT
        }
        if (throwable == null) {
            printer.invoke(tag, "${logStr.substring(start)}\n$LINE\n")
        } else {
            trPrinter.invoke(tag, "${logStr.substring(start)}\n$LINE\n", throwable)
        }
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
fun logWtf(msg: Any?, tag: String = LogUtil.defaultTag, throwable: Throwable? = null) {
    LogUtil.printLog(tag, msg.toString(), throwable, Log.ASSERT)
}