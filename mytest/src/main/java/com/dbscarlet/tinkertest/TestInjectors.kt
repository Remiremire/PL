package com.dbscarlet.tinkertest

import com.dbscarlet.tinkertest.core.FindPatchAct
import com.dbscarlet.tinkertest.core.VersionInfoAct
import com.dbscarlet.tinkertest.core.apiTest.ApiTestAct
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