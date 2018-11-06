package com.dbscarlet.mediademo

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mediademo.databinding.ActMediaTestBinding

/**
 * Created by Daibing Wang on 2018/11/6.
 */
@Route(path = ActPath.Media.MEDIA_MAIN)
class MediaMainAct: BaseActivity<ActMediaTestBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_media_test
    }

    override fun initView() {
        binding.tvCameraTest.setOnClickListener {
            ARouter.getInstance()
                    .build(ActPath.Media.CAMERA_TEST)
                    .navigation(this)
        }
    }
}