package com.dbscarlet.mytest.core.authentication

import android.app.Dialog
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.twitterApiConfig.DEF_OAUTH_TOKEN
import com.dbscarlet.applib.twitterApiConfig.DEF_OAUTH_TOKEN_SECRET
import com.dbscarlet.applib.twitterApiConfig.OAUTH_TOKEN
import com.dbscarlet.applib.twitterApiConfig.OAUTH_TOKEN_SECRET
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.util.logI
import com.dbscarlet.mytest.R
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.act_authentication.*


/**
 * Created by Daibing Wang on 2018/7/18.
 */
@Route(path = Path.TEST.AUTHENTICATION)
class AuthenticationAct: CommonActivity() {
    var authenticityToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_authentication)
        btn_default_token.setOnClickListener {
            OAUTH_TOKEN = DEF_OAUTH_TOKEN
            OAUTH_TOKEN_SECRET = DEF_OAUTH_TOKEN_SECRET
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

                        override fun onError(response: Response<String>?) {
                            super.onError(response)
                        }
                    })

        }
        btn_ask_user.setOnClickListener {
            OkGo.get<String>("https://api.twitter.com/oauth/authorize")
                    .params("oauth_token", OAUTH_TOKEN)
                    .execute(object : StringCallback(){
                        override fun onSuccess(response: Response<String>) {
                            val body = response.body()
                            parseAuthenticityToken(body)
                            showWebDialog(body)
                        }
                    })
        }
        btn_login.setOnClickListener {
            if (authenticityToken.isNullOrEmpty() ||
                    et_email.text.isNullOrEmpty() ||
                    et_psw.text.isNullOrEmpty()) {
                Toast.makeText(this, "not ready", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            OkGo.post<String>("https://api.twitter.com/oauth/authorize")
                    .params("authenticity_token", authenticityToken)
                    .params("session[username_or_email]", et_email.text.toString())
                    .params("session[password]", et_psw.text.toString())
                    .execute(object : StringCallback(){
                        override fun onSuccess(response: Response<String>?) {
                            logI("login result\n${response?.body()}")
                        }
                    })
        }
    }

    private fun parseAuthenticityToken(html: String) {
        val nameAttrRegex = "name\\s*?=\\s*?\"authenticity_token\""
        val valueAttrRegex = "value\\s*?=\\s*?\"[0-9a-zA-Z_\\-]+\""
        val tokenLabel = Regex("<[^<>]*?$nameAttrRegex[^<>]*?$valueAttrRegex[^<>]*?>")
                .find(html)?.value
        if (tokenLabel == null) {
            logI("match nothing")
            return
        }
        val valueAttr = Regex(valueAttrRegex).find(tokenLabel)?.value
        val token = valueAttr?.subSequence(valueAttr.indexOf("\"") + 1, valueAttr.lastIndexOf("\""))
        logI("find token:  $token")
        if (token != null) authenticityToken = token.toString()
    }

    private fun showWebDialog(html: String) {
        val dialog = Dialog(this)
        val webView = WebView(this)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient  = WebViewClient()


        val settings = webView.settings
        // 此方法需要启用JavaScript
        settings.javaScriptEnabled = true

        // 把刚才的接口类注册到名为HTMLOUT的JavaScript接口
        webView.addJavascriptInterface(MyJavaScriptInterface(), "HTMLOUT")

        // 必须在loadUrl之前设置WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 这里可以过滤一下url
                webView.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);")
            }
        }

        // 开始加载网址
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)


        dialog.setContentView(webView)
        dialog.show()
    }

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processHTML(html: String) {
            // 在这里处理html源码
            logI(html)
        }
    }

}