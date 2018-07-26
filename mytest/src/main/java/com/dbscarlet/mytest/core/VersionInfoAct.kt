package com.dbscarlet.mytest.core

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
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
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.core.curve.CurveLine
import com.dbscarlet.mytest.core.curve.XAxes
import com.dbscarlet.mytest.databinding.ActVersionInfoBinding
import kotlinx.android.synthetic.main.act_version_info.*
import java.io.File
import java.util.*

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
//        setContentView(R.layout.act_version_info)
        val binding = DataBindingUtil.setContentView<ActVersionInfoBinding>(this, R.layout.act_version_info)
        binding.versionInfo = AppInfo.VERSION_NAME
        binding.patchInfo = "Patch_$patchCode"
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
        btn_authentication.setOnClickListener {
            ARouter.getInstance()
                    .build(Path.AUTHENTICATION)
                    .navigation()
        }
        TinkerUtil.tinkerInstallCallback = this
        setCurveView()
    }

    private fun setCurveView() {
        val xAxes = XAxes(this)
        val labels = mutableListOf<String>()
        for (i in 1..10) {
            labels.add("X$i")
        }
        xAxes.setLabels(labels)
        curve_view.setXAxes(xAxes)
        var showPoint : CurveLine.Point? = null
        curve_view.setOnPointTouch {
            _, _, point ->
            when {
                showPoint == null -> {
                    showPoint = point
                    point.show = true
                }
                showPoint === point -> {
                    showPoint = null
                    point.show = false
                }
                else -> {
                    showPoint?.show = false
                    showPoint = point
                    point.show = true
                }
            }
            curve_view.notifyCurvesChange()
        }
        curve_view.setBaseInfo(0f, 10f)
        val curveLineList = mutableListOf<CurveLine>()
        curve_view.setCurveLine(curveLineList)
        val redLine = CurveLine(this, Color.parseColor("#FF7875"))
        val blueLine = CurveLine(this, Color.parseColor("#40A9FF"))
        val yellowLine = CurveLine(this, Color.parseColor("#FFC53D"))
        curveLineList.add(redLine)
        curveLineList.add(blueLine)
        curveLineList.add(yellowLine)
        redLine.pointList = randomPoint()
        blueLine.pointList = randomPoint()
        yellowLine.pointList = randomPoint()
        Handler().postDelayed({
            curve_view.notifyCurvesChange()
        }, 500)
    }

    private fun randomPoint(): MutableList<CurveLine.Point> {
        val random = Random()
        val result = mutableListOf<CurveLine.Point>()
        for (i in 0..9) {
            val value = (random.nextFloat() * 1000).toInt().toFloat() / 100
            result.add(CurveLine.Point(value, false, value.toString()))
        }
        return result
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