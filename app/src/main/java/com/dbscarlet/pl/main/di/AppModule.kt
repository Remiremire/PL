package com.dbscarlet.pl.main.di

import com.dbscarlet.common.di.CommonAppModule
import com.dbscarlet.pl.main.application.App
import dagger.Module

/**
 * Created by Daibing Wang on 2018/9/17.
 */
@Module
class AppModule(app: App): CommonAppModule<App>(app)