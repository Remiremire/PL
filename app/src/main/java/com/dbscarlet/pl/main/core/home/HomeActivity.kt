package com.dbscarlet.pl.main.core.home


import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.Path
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.pl.BuildConfig
import com.dbscarlet.pl.R
import kotlinx.android.synthetic.main.activity_home.*

@Route(path = Path.APP.HOME)
class HomeActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        tv_tinker_test.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.TEST.TINKER_TEST)
                    .withInt("patchCode", BuildConfig.TINKER_PATCH_VERSION)
                    .navigation(this)
        }
        tv_hello.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.APP.AUTHORIZE)
                    .navigation(this)
        }
    }
}