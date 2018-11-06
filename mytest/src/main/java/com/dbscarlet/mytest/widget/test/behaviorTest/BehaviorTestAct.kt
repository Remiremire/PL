package com.dbscarlet.mytest.widget.test.behaviorTest

import android.databinding.ViewDataBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mytest.R

/**
 * Created by Daibing Wang on 2018/10/29.
 */
@Route(path = ActPath.TEST.BEHAVIOR_TEST)
class BehaviorTestAct: BaseActivity<ViewDataBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_behavior_test
    }

}