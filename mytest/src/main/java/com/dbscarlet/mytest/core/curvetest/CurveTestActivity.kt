package com.dbscarlet.mytest.core.curvetest

import android.databinding.ViewDataBinding
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mytest.R

/**
 * Created by Daibing Wang on 2018/8/24.
 */
class CurveTestActivity: BaseActivity<ViewDataBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_curve_test
    }
}