package com.dbscarlet.mytest.core

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.Path
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.util.AppInfo
import com.dbscarlet.common.util.InstallCallback
import com.dbscarlet.common.util.InstallResult
import com.dbscarlet.common.util.TinkerUtil
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.databinding.ActVersionInfoBinding
import kotlinx.android.synthetic.main.act_version_info.*
import java.io.File

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Route(path = Path.TEST.TINKER_TEST)
class VersionInfoAct: CommonActivity(), InstallCallback {

    @JvmField
    @Autowired(name = "patchCode")
    var patchCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.act_version_info)
        val binding = DataBindingUtil.setContentView<ActVersionInfoBinding>(this, R.layout.act_version_info)
        binding.versionInfo = AppInfo.VERSION_NAME
        binding.patchInfo = "Patch_$patchCode"
        btn_install_patch.setOnClickListener{
            ARouter.getInstance()
                    .build(Path.TEST.FIND_PATCH_FILE)
                    .navigation(this, 101)
        }
        btn_api_test.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.TEST.API_TEST)
                    .navigation(this)
        }
        btn_authentication.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.TEST.AUTHENTICATION)
                    .navigation(this)
        }
        btn_curve_test.setOnClickListener{
            ARouter.getInstance()
                    .build(Path.TEST.CURVE_TEST)
                    .navigation(this)
        }
        btn_widget_test.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.TEST.WIDGET_TEST)
                    .navigation()
        }
        btn_tweet_test.setOnClickListener{
            ARouter.getInstance()
                    .build(Path.TWEETS.TWEET_TEST)
                    .navigation()
        }
        TinkerUtil.tinkerInstallCallback = this
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            101->{
                if (resultCode == Activity.RESULT_OK) {
                    val patchFile = data.getSerializableExtra("patchFile") as File
                    TinkerUtil.installTinkerPatch(this, patchFile.absolutePath)
                }
            }
        }
    }

    override fun onInstallResult(installResult: InstallResult) {
        if (installResult.isSuccess) {
            TinkerUtil.showRestartDialog(this)
        }
    }
}