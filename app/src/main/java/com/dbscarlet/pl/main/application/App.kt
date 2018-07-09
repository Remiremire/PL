package com.dbscarlet.pl.main.application

import com.dbscarlet.common.basic.CommonApp
import com.dbscarlet.pl.main.di.DaggerAppComponent

/**
 * Created by Daibing Wang on 2018/5/10.
 */
class App : CommonApp() {

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .build()
                .inject(this)
    }
}