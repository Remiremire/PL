package com.dbscarlet.tinkertest.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.basic.IPresenter
import com.dbscarlet.common.util.AppInfo
import com.dbscarlet.common.util.InstallCallback
import com.dbscarlet.common.util.InstallResult
import com.dbscarlet.common.util.TinkerUtil
import com.dbscarlet.tinkertest.R
import kotlinx.android.synthetic.main.act_version_info.*
import java.io.File

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Route(path = Path.TINKER_TEST)
class VersionInfoAct: BaseActivity(), InstallCallback {

    @JvmField
    @Autowired(name = "patchCode")
    var patchCode: Int = 0

    override fun getPresenters(): Array<IPresenter<*>>? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_version_info)
        tv_version.text = AppInfo.VERSION_NAME
        tv_patch.text = "Patch_$patchCode"
        btn_install_patch.setOnClickListener{
            ARouter.getInstance()
                    .build(Path.FIND_PATCH_FILE)
                    .navigation(this, 101)
        }
        btn_api_test.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.API_TEST)
                    .navigation(this)
        }
        TinkerUtil.tinkerInstallCallback = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            101->{
                if (resultCode == Activity.RESULT_OK) {
                    val patchFile = data?.getSerializableExtra("patchFile") as File
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