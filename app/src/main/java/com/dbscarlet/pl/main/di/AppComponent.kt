package com.dbscarlet.pl.main.di

import com.dbscarlet.mediademo.di.MediaInjectors
import com.dbscarlet.mytest.TestInjectors
import com.dbscarlet.pl.main.application.App
import com.diwinet.xhs.tweets.di.TweetInjectors
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
        AppModule::class,
        AppInjectors::class,
        TestInjectors::class,
        TweetInjectors::class,
        MediaInjectors::class
])
interface AppComponent {
    fun inject(app: App)
}