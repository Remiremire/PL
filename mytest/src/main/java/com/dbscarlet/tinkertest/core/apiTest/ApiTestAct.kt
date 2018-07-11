package com.dbscarlet.tinkertest.core.apiTest

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.basic.IPresenter
import com.dbscarlet.common.util.logI
import com.dbscarlet.tinkertest.R
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.act_api_test.*

/**
 * Created by Daibing Wang on 2018/7/10.
 */
@Route(path = Path.API_TEST)
class ApiTestAct: BaseActivity() {
    override fun getPresenters(): Array<IPresenter<*>>? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_api_test)
        tv_video_list.setOnClickListener {
            OkGo.get<String>("http://api.bilibili.cn/list")
                    .params("ts", (System.currentTimeMillis() / 1000).toInt())
                    .params("page", 1)
                    .params("pagesize", 10)
                    .execute(object : StringCallback(){
                        override fun onSuccess(response: Response<String>?) {
                            logI(response?.body() ?: "")
                        }
                    })
        }
    }
}