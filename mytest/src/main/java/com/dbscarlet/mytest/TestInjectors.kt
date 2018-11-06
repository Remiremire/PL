package com.dbscarlet.mytest

import com.dbscarlet.mytest.core.FindPatchAct
import com.dbscarlet.mytest.core.VersionInfoAct
import com.dbscarlet.mytest.core.apiTest.ApiTestAct
import com.dbscarlet.mytest.core.authentication.AuthenticationAct
import com.dbscarlet.mytest.core.curvetest.CurveTestActivity
import com.dbscarlet.mytest.widget.test.behaviorTest.BehaviorTestAct
import com.dbscarlet.mytest.widget.test.WidgetTestAct
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

    @ContributesAndroidInjector
    abstract fun authentication(): AuthenticationAct

    @ContributesAndroidInjector
    abstract fun curveTest(): CurveTestActivity

    @ContributesAndroidInjector
    abstract fun widgetTest(): WidgetTestAct

    @ContributesAndroidInjector
    abstract fun behaviorTest(): BehaviorTestAct
}