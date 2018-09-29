package com.diwinet.xhs.tweets.webservice

import com.dbscarlet.applib.contact.TwitterUrl
import com.dbscarlet.common.network.params
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.bean.TimelineRequest
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/27.
 */

class TweetsWebService
    @Inject
    constructor() {
    fun homeTimeLine(timelineRequest: TimelineRequest): Request<List<HomeTimelineTweet>, *> {
        return OkGo.get<List<HomeTimelineTweet>>(TwitterUrl.HOME_TIMELINE)
                .params(timelineRequest)
    }
}