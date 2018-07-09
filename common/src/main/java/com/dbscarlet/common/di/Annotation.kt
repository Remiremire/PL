package com.dbscarlet.common.di

import javax.inject.Scope

/**
 * Created by Daibing Wang on 2018/6/20.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment