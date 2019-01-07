package com.dbscarlet.pl.main.core.authorize

import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.dataResource.ResObserver
import com.dbscarlet.common.dataResource.Success
import com.dbscarlet.common.commonUtil.toastShort
import com.dbscarlet.pl.R
import com.dbscarlet.pl.databinding.ActAuthorizeBinding
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/14.
 */
@Route(path = ActPath.App.AUTHORIZE)
class AuthorizeAct: BaseActivity<ActAuthorizeBinding>() {
    @Inject
    lateinit var authorizeVM: AuthorizeVM

    override fun getContentLayout(): Int {
        return R.layout.act_authorize
    }

    override fun initView() {
        binding.btnLogin.setOnClickListener {
            val pinCode = binding.pinCode
            if (pinCode == null || pinCode.isEmpty()) {
                toastShort("请先输入验证码")
                return@setOnClickListener
            }
            authorizeVM.login(pinCode)
                    .observe(this, object : ResObserver<Unit>(){
                        override fun onSuccess(res: Success<Unit>) {
                            toastShort("登录授权成功")
                        }
                    })
        }
    }

    override fun initData() {
        authorizeVM.getLoginHtml()
                .observe(this, object : ResObserver<String>(){
                    override fun onSuccess(res: Success<String>) {
                        showWebDialog(binding.web, res.data)
                    }
                })
    }

    private fun showWebDialog(webView: WebView, html: String) {
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
    }

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processHTML(html: String) {
            // 在这里处理html源码
//            logI(html)
        }
    }
}