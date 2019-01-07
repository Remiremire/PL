package com.dbscarlet.common.commonUtil

import android.app.Application

/**
 * Created by Daibing Wang on 2019/1/4.
 */
object UtilsConfig {
    fun initUtils(app: Application) {
        AppInfo.init(app)
        SpSave.sharePreferences = app.getSharedPreferences(SpSave.SP_NAME, SpSave.MODE)
        LogUtil.debug = AppInfo.DEBUG
        displayMetrics = app.resources.displayMetrics
    }
}