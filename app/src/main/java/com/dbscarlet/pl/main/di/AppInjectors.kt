package com.dbscarlet.pl.main.di

import com.dbscarlet.pl.main.core.authorize.AuthorizeAct
import com.dbscarlet.pl.main.core.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Daibing Wang on 2018/6/28.
 */
@Module
abstract class AppInjectors {
    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun ahthorizeAct(): AuthorizeAct
}