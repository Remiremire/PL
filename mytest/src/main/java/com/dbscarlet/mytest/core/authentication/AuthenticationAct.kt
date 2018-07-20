package com.dbscarlet.mytest.core.authentication

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.basic.IPresenter
import com.dbscarlet.common.util.logI
import com.dbscarlet.mytest.R
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.act_authentication.*
import java.util.regex.Pattern

/**
 * Created by Daibing Wang on 2018/7/18.
 */
@Route(path = Path.AUTHENTICATION)
class AuthenticationAct: BaseActivity() {
    var loginHtml: String? = null
    var loginSuccessHtml: String? = null

    override fun getPresenters(): Array<IPresenter<*>>? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_authentication)
        btn_default_token.setOnClickListener {
            OkGo.get<String>("https://api.twitter.com/oauth/request_token")
                    .execute(object : StringCallback(){
                        override fun onSuccess(response: Response<String>?) {
                            val body = response?.body()
                            logI("temp token response:\n$body")
                            if (body != null) {
                                val results = body.split('&')
                                val resultMap = mutableMapOf<String, String>()
                                results.forEach{
                                    val p = it.split('=')
                                    if (p.size == 2) {
                                        resultMap[p[0]] = p[1]
                                    }
                                }
                                if (resultMap["oauth_callback_confirmed"] == "true") {
                                    OAUTH_TOKEN = resultMap["oauth_token"] ?: DEF_OAUTH_TOKEN
                                    OAUTH_TOKEN_SECRET = resultMap["oauth_token_secret"] ?: DEF_OAUTH_TOKEN_SECRET
                                }
                            }
                        }
                    })
        }
        btn_ask_user.setOnClickListener {
            OkGo.get<String>("https://api.twitter.com/oauth/authorize")
                    .params("oauth_token", OAUTH_TOKEN)
                    .execute(object : StringCallback(){
                        override fun onSuccess(response: Response<String>) {
                            val body = response.body()
                            loginHtml = body

                            logI("Login Html:\n$body")
                        }
                    })
        }
        btn_match.setOnClickListener {
            if (loginHtml.isNullOrEmpty()) return@setOnClickListener
            val pattern = Pattern.compile("<.*?name\\s*=\\s*\"authenticity_token\"")
            val matcher = pattern.matcher(loginHtml)
            logI("mathes result: ${matcher.matches()}")
        }
    }



}