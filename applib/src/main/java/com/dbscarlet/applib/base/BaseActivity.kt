package com.dbscarlet.applib.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.dbscarlet.common.basic.CommonActivity

/**
 * Created by Daibing Wang on 2018/7/3.
 */
abstract class BaseActivity<T: ViewDataBinding>: CommonActivity() {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getContentLayout())
        initView()
        initData()
    }

    @LayoutRes
    abstract fun getContentLayout(): Int

    open fun initView(){}

    open fun initData(){}
}