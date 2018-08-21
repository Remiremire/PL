package com.dbscarlet.common.basic

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/4/24.
 */
abstract class CommonActivity : AppCompatActivity(), HasFragmentInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector : DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return supportFragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)

    }

    /////////////////////////动态权限相关////////////////////////////////////////////////


}