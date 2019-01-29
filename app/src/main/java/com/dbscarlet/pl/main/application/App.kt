package com.dbscarlet.pl.main.application

import android.content.Context
import com.dbscarlet.applib.contact.SPSaveKey
import com.dbscarlet.applib.twitterNetwork.setToken
import com.dbscarlet.common.basic.CommonApp
import com.dbscarlet.common.commonUtil.logI
import com.dbscarlet.pl.main.di.AppModule
import com.dbscarlet.pl.main.di.DaggerAppComponent


/**
 * Created by Daibing Wang on 2018/5/10.
 */
class App : CommonApp() {

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
                .inject(this)
        initNetwork()
    }

    private fun initNetwork() {
        val sp = getSharedPreferences(SPSaveKey.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val oauthToken = sp.getString(SPSaveKey.OAUTH_TOKEN, null)
        val secret = sp.getString(SPSaveKey.OAUTH_TOKEN_SECRET, null)
        if (oauthToken != null && secret != null) {
            setToken(oauthToken, secret)
            logI("load token: $oauthToken\nsecret: $secret" )
        }
    }

}