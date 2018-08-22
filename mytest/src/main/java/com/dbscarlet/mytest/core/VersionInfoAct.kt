package com.dbscarlet.mytest.core

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
import com.dbscarlet.applib.curve.CurveLineUpdater
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
        val width = 10
        for (i in 1..width) {
            labels.add("X$i")
        }
        xAxes.setLabels(labels)
        curve_view.setXAxes(xAxes)
        curve_view.setOnSelectPointListener(CurveView.ShowLastSelectPointListener())
        curve_view.setValueLimit(-0.3f, 10.3f)
        val lineList = mutableListOf<CurveLine>()
        curve_view.setCurveLine(lineList)
        val redLine = createLine(lineList, curve_view, Color.parseColor("#FF7875"), width)
        val blueLine = createLine(lineList, curve_view, Color.parseColor("#40A9FF"), width)
        val yellowLine = createLine(lineList, curve_view, Color.parseColor("#FFC53D"), width)
        curve_view.notifyChange()
        class Updater: CurveLineUpdater<Float>() {
            override fun convert(data: Float): CurveLine.Point {
                return CurveLine.Point(data)
            }
        }
        val redUpdater = Updater()
        val blueUpdater = Updater()
        val yellowUpdater = Updater()

        redUpdater.setCurveInfo(curve_view, redLine, width)
        blueUpdater.setCurveInfo(curve_view, blueLine, width)
        yellowUpdater.setCurveInfo(curve_view, yellowLine, width)

        val random = Random()
        var count  = 1
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                if (redUpdater.cashSize < 5) {
                    redUpdater.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
                if (blueUpdater.cashSize < 5) {
                    blueUpdater.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
                if (yellowUpdater.cashSize < 5) {
                    yellowUpdater.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
                count++
                if (count % 10 == 0) {
                    sendEmptyMessageDelayed(1, 10000)
                } else {
                    sendEmptyMessageDelayed(1, 500)
                }
            }
        }
        handler.sendEmptyMessage(1)
    }

    private fun createLine(list: MutableList<CurveLine>, curveView: CurveView, color: Int, width: Int): CurveLine {
        val line = CurveLine(this, color)
        list.add(line)
        line.pointList = randomPoint()
        return line
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