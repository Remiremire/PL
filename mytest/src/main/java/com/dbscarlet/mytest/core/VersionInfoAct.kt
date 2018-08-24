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
import com.dbscarlet.applib.curve.*
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
    var updateThread: UpdateThread? = null

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

    override fun onStart() {
        super.onStart()
        updateThread?.startUpdate()
    }

    override fun onStop() {
        super.onStop()
        updateThread?.stopUpdate()
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
        val redLine = createLine(lineList, Color.parseColor("#FF7875"))
        val blueLine = createLine(lineList, Color.parseColor("#40A9FF"))
        val yellowLine = createLine(lineList, Color.parseColor("#FFC53D"))
        curve_view.notifyChange()
        class Updater: CurveLineUpdater<Float>(10) {
            override fun convert(data: Float): CurveLine.Point {
                return CurveLine.Point(data)
            }
        }
        val redUpdater = Updater()
        val blueUpdater = Updater()
        val yellowUpdater = Updater()

        redUpdater.bindCurveLine(redLine)
        blueUpdater.bindCurveLine(blueLine)
        yellowUpdater.bindCurveLine(yellowLine)

        redUpdater.addDataList(randomFloatList())
        blueUpdater.addDataList(randomFloatList())
        yellowUpdater.addDataList(randomFloatList())

        updateThread = UpdateThread()
        updateThread?.setCurveView(curve_view)
        updateThread?.addUpdater(redUpdater)
        updateThread?.addUpdater(blueUpdater)
        updateThread?.addUpdater(yellowUpdater)

        val random = Random()
        var count  = 1
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                if (redUpdater.cacheSize < 3) {
                    redUpdater.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
                if (blueUpdater.cacheSize < 3) {
                    blueUpdater.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
                if (yellowUpdater.cacheSize < 3) {
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

    private fun createLine(list: MutableList<CurveLine>, color: Int): CurveLine {
        val line = CurveLine(this, color)
        list.add(line)
        return line
    }

    private fun randomFloatList(): MutableList<Float> {
        val random = Random()
        val result = mutableListOf<Float>()
        for (i in 0..12) {
            result.add((random.nextFloat() * 1000).toInt().toFloat() / 100)
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