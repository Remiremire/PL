package com.diwinet.xhs.tweets.webservice

import com.dbscarlet.applib.contact.TwitterUrl
import com.diwinet.xhs.tweets.bean.HomeTimeline
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/27.
 */
class TweetsWebService
    @Inject
    constructor() {
    fun homeTimeLine(count: Int = 20, since_id: String? = null,
                     max_id: String? = null, trim_user: Boolean = false,
                     exclude_replies: Boolean = false, include_entities: Boolean = true): Request<HomeTimeline, *> {
        return OkGo.get<HomeTimeline>(TwitterUrl.HOME_TIMELINE)
                .params("count", count)
                .params("since_id", since_id)
                .params("max_id", max_id)
                .params("trim_user", trim_user)
                .params("exclude_replies", exclude_replies)
                .params("include_entities", include_entities)
    }
}