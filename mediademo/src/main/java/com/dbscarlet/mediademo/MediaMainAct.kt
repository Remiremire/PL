package com.dbscarlet.mediademo

import android.databinding.ViewDataBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity

/**
 * Created by Daibing Wang on 2018/11/6.
 */
@Route(path = ActPath.Media.MEDIA_MAIN)
class MediaMainAct: BaseActivity<ViewDataBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_media_test
    }

}