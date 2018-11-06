package com.dbscarlet.mytest.core.curvetest

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.applib.curve.*
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.databinding.ActCurveTestBinding
import java.util.*

/**
 * Created by Daibing Wang on 2018/8/24.
 */
@Route(path = ActPath.Test.CURVE_TEST)
class CurveTestActivity: BaseActivity<ActCurveTestBinding>() {
    private val updaterList = mutableListOf<PointUpdater>()
    private val updateThread = UpdateThread()
    private val createDataHandler = object : Handler(Looper.getMainLooper()) {
        val random = Random()
        override fun handleMessage(msg: Message?) {
            updaterList.forEach {
                if (it.cacheSize < 5) {
                    it.addData((random.nextFloat() * 1000).toInt().toFloat() / 100)
                }
            }
            sendEmptyMessageDelayed(1, 500)
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.act_curve_test
    }

    override fun initView() {
        binding.rcvContent.layoutManager = LinearLayoutManager(this)
        binding.rcvContent.adapter = Adapter()
    }

    override fun initData() {

        for (i in 1..18) {
            val updater = PointUpdater(null, null)
            updateThread.addUpdater(updater)
            updaterList.add(updater)
        }

    }

    override fun onStart() {
        super.onStart()
        createDataHandler.sendEmptyMessage(1)
        updateThread.startUpdate()
    }

    override fun onStop() {
        super.onStop()
        createDataHandler.removeMessages(1)
        updateThread.stopUpdate()
    }

    class PointUpdater(var curveView: CurveView? , var line: CurveLine?): Updater<Float, CurveLine.Point>(10) {
        override fun onUpdate(dataList: MutableList<CurveLine.Point>?, offset: Float) {
            line?.pointList = dataList
            line?.setLineLeftOffset(offset)
            curveView?.notifyChange()
        }

        override fun convert(data: Float): CurveLine.Point {
            return CurveLine.Point(data)
        }
    }

    inner class Adapter: RecyclerView.Adapter<Adapter.Holder>() {
        override fun getItemCount(): Int {
            return updaterList.size
        }

        override fun onBindViewHolder(holder: Holder?, position: Int) {
            val line = CurveLine(this@CurveTestActivity, Color.GREEN)
            holder?.curveView?.setCurveLine(listOf(line))
            updaterList[position].curveView = holder?.curveView
            updaterList[position].line = line
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            val view = layoutInflater.inflate(R.layout.item_curve_test, parent, false)
            return Holder(view)
        }

        inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val curveView: CurveView = itemView.findViewById(R.id.curve_view)
            init {
                val xAxes = XAxes(this@CurveTestActivity)
                xAxes.setLabels(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                curveView.setXAxes(xAxes)
                curveView.setValueLimit(-1f, 11f)
                curveView.setOnSelectPointListener(CurveView.ShowLastSelectPointListener())
            }
        }
    }
}