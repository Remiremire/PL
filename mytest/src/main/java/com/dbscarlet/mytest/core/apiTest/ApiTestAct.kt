package com.dbscarlet.mytest.core.apiTest

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.util.logI
import com.dbscarlet.mytest.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.act_api_test.*

/**
 * Created by Daibing Wang on 2018/7/10.
 */
@Route(path = ActPath.TEST.API_TEST)
class ApiTestAct: CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_api_test)
        tv_video_list.setOnClickListener {
            val testObj = Gson().fromJson("{\"name\": \"aaa\", \"age\": 14}", TestObj::class.java)
            logI(testObj)
        }

    }

    class TestObj{
        val name: String = ""
        val age: Int = 0
        override fun toString(): String {
            return "TestObj(name='$name', age=$age)"
        }


    }
}