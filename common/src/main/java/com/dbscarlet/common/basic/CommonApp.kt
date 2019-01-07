package com.dbscarlet.common.basic

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.support.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.common.commonUtil.UtilsConfig
import com.dbscarlet.common.commonUtil.TinkerUtil
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/6/20.
 */
open class CommonApp : Application(), HasActivityInjector, HasFragmentInjector, HasSupportFragmentInjector,
        HasServiceInjector, HasBroadcastReceiverInjector, HasContentProviderInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>
    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    lateinit var contentProviderInjector: DispatchingAndroidInjector<ContentProvider>



    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityInjector
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment>? {
        return supportFragmentInjector
    }

    override fun serviceInjector(): AndroidInjector<Service>? {
        return serviceInjector
    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver>? {
        return broadcastReceiverInjector
    }

    override fun contentProviderInjector(): AndroidInjector<ContentProvider>? {
        return contentProviderInjector
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        TinkerUtil.initTinker(this)
//        ARouter.openLog()    // 打印日志
//        ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
        UtilsConfig.initUtils(this)
    }
}