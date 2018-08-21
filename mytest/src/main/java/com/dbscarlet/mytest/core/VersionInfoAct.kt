package com.dbscarlet.mytest.core

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.curve.CurveLine
import com.dbscarlet.applib.curve.CurveView
import com.dbscarlet.applib.curve.XAxes
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.util.AppInfo
import com.dbscarlet.common.util.InstallCallback
import com.dbscarlet.common.util.InstallResult
import com.dbscarlet.common.util.TinkerUtil
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.databinding.ActVersionInfoBinding
import kotlinx.android.synthetic.main.act_version_info.*
import java.io.File
import java.util.*

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
        curve_view.setOnSelectPointListener(CurveView.ShowLastSelectPointListener())
        curve_view.setValueLimit(0f, 12f)
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
        redLine.setLineLeftOffset(-1f)
        blueLine.setLineLeftOffset(-1f)
        yellowLine.setLineLeftOffset(-1f)
        curve_view.notifyChange()

        val random = Random()
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                redLine.pointList.removeAt(0)
                blueLine.pointList.removeAt(0)
                yellowLine.pointList.removeAt(0)
                redLine.pointList.add(CurveLine.Point((random.nextFloat() * 1000).toInt().toFloat() / 100))
                blueLine.pointList.add(CurveLine.Point((random.nextFloat() * 1000).toInt().toFloat() / 100))
                yellowLine.pointList.add(CurveLine.Point((random.nextFloat() * 1000).toInt().toFloat() / 100))
                redLine.setLineLeftOffset(-1f)
                blueLine.setLineLeftOffset(-1f)
                yellowLine.setLineLeftOffset(-1f)
                curve_view.notifyChange()

                val animator = ValueAnimator.ofFloat(-1f, -2f)
                animator.addUpdateListener {
                    val offset = it.animatedValue as Float
                    redLine.setLineLeftOffset(offset)
                    blueLine.setLineLeftOffset(offset)
                    yellowLine.setLineLeftOffset(offset)
                    curve_view.notifyChange()
                }
                animator.duration = 1100
                animator.start()
                sendEmptyMessageDelayed(1, 1000)
            }
        }
        handler.sendEmptyMessageDelayed(1, 1000)

    }

    private fun randomPoint(): MutableList<CurveLine.Point> {
        val random = Random()
        val result = mutableListOf<CurveLine.Point>()
        for (i in 0..12) {
            val value = (random.nextFloat() * 1000).toInt().toFloat() / 100
            result.add(CurveLine.Point(value, false, value.toString()))
        }
        return result
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