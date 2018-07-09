package com.dbscarlet.common.util

import android.app.Application
import android.content.pm.ApplicationInfo

/**
 * Created by Daibing Wang on 2018/6/28.
 */
object AppInfo {
    var DEBUG: Boolean = true
        private set
    var PACKAGE_NAME: String = ""
        private set
    var VERSION: Int = 0
        private set
    var VERSION_NAME: String = ""
        private set

    internal fun init(app: Application) {
        val appInfo = app.applicationInfo
        DEBUG = appInfo != null &&
                (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        PACKAGE_NAME = app.packageName
        val packageInfo = app.packageManager.getPackageInfo(PACKAGE_NAME, 0)
        VERSION = packageInfo.versionCode
        VERSION_NAME = packageInfo.versionName
    }
}
