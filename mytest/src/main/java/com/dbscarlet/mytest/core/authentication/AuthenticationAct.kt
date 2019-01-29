package com.dbscarlet.mytest.core.authentication

import android.app.Dialog
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.commonUtil.logI
import com.dbscarlet.mytest.R
import kotlinx.android.synthetic.main.act_authentication.*


/**
 * Created by Daibing Wang on 2018/7/18.
 */
@Route(path = ActPath.Test.AUTHENTICATION)
class AuthenticationAct: CommonActivity() {

    private var token: String? = null

    private var secret: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_authentication)
        btn_default_token.setOnClickListener {

        }
        btn_ask_user.setOnClickListener {
            if (token.isNullOrEmpty() || secret.isNullOrEmpty()) {
                return@setOnClickListener
            }
        }
    }

    private fun showWebDialog(html: String) {
        val dialog = Dialog(this)
        val webView = WebView(this)
        webView.setWebChromeClient(WebChromeClient())

        val settings = webView.settings
        // 此方法需要启用JavaScript
        settings.javaScriptEnabled = true

        // 把刚才的接口类注册到名为HTMLOUT的JavaScript接口
        webView.addJavascriptInterface(MyJavaScriptInterface(), "HTMLOUT")

        // 必须在loadUrl之前设置WebViewClient
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 这里可以过滤一下url
                webView.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);")
            }
        })

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