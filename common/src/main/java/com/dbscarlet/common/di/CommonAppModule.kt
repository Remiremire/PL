package com.dbscarlet.common.di

import android.content.Context
import com.dbscarlet.common.basic.CommonApp
import dagger.Module
import dagger.Provides

/**
 * Created by Daibing Wang on 2018/6/20.
 */
@Module
abstract class CommonAppModule<T : CommonApp>(private var application: T) {
    @Provides
    fun getApp() : T {
        return application
    }

    @Provides
    fun getContext() : Context {
        return application
    }
}