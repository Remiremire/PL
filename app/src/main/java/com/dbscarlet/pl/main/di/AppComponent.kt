package com.dbscarlet.pl.main.di

import com.dbscarlet.pl.main.application.App
import com.dbscarlet.tinkertest.TinkerTestInjectors
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Daibing Wang on 2018/6/28.
 */
@Singleton
@Component(modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppInjectors::class,
        TinkerTestInjectors::class
])
interface AppComponent {
    fun inject(app: App)
}