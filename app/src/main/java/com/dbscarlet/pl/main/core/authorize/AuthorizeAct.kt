package com.dbscarlet.pl.main.core.authorize

import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.pl.R
import com.dbscarlet.pl.databinding.ActAuthorizeBinding

/**
 * Created by Daibing Wang on 2018/8/14.
 */
@Route(path = Path.APP.AUTHORIZE)
class AuthorizeAct: BaseActivity<ActAuthorizeBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_authorize
    }

    override fun initView() {

    }
}