package com.dbscarlet.mytest

import com.dbscarlet.mytest.core.FindPatchAct
import com.dbscarlet.mytest.core.VersionInfoAct
import com.dbscarlet.mytest.core.apiTest.ApiTestAct
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Module
abstract class TestInjectors {
    @ContributesAndroidInjector
    abstract fun versionInfoAct(): VersionInfoAct

    @ContributesAndroidInjector
    abstract fun findPatchAct(): FindPatchAct

    @ContributesAndroidInjector
    abstract fun apiTestAct(): ApiTestAct
}