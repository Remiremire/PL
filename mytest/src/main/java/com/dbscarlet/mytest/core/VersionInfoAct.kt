package com.dbscarlet.mytest.core

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.ActPath
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.commonUtil.AppInfo
import com.dbscarlet.common.commonUtil.InstallCallback
import com.dbscarlet.common.commonUtil.InstallResult
import com.dbscarlet.common.commonUtil.TinkerManager
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.databinding.ActVersionInfoBinding
import kotlinx.android.synthetic.main.act_version_info.*
import java.io.File

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Route(path = ActPath.Test.TINKER_TEST)
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
                    .build(ActPath.Test.FIND_PATCH_FILE)
                    .navigation(this, 101)
        }
        btn_api_test.setOnClickListener {
            ARouter.getInstance()
                    .build(ActPath.Test.API_TEST)
                    .navigation(this)
        }
        btn_authentication.setOnClickListener {
            ARouter.getInstance()
                    .build(ActPath.Test.AUTHENTICATION)
                    .navigation(this)
        }
        btn_curve_test.setOnClickListener{
            ARouter.getInstance()
                    .build(ActPath.Test.CURVE_TEST)
                    .navigation(this)
        }
        btn_widget_test.setOnClickListener {
            ARouter.getInstance()
                    .build(ActPath.Test.WIDGET_TEST)
                    .navigation()
        }
        btn_tweet_test.setOnClickListener{
            ARouter.getInstance()
                    .build(ActPath.Tweet.TWEET_TEST)
                    .navigation()
        }
        TinkerManager.tinkerInstallCallback = this
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            101->{
                if (resultCode == Activity.RESULT_OK) {
                    val patchFile = data?.getSerializableExtra("patchFile") as File
                    TinkerManager.installTinkerPatch(this, patchFile.absolutePath)
                }
            }
        }
    }

    override fun onInstallResult(installResult: InstallResult) {
        if (installResult.isSuccess) {
            TinkerManager.showRestartDialog(this)
        }
    }
}