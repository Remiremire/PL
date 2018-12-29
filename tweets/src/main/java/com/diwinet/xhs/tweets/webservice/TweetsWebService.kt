package com.diwinet.xhs.tweets.webservice

import com.dbscarlet.common.dataResource.ResLiveData
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.bean.TimelineRequest
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/8/27.
 */

class TweetsWebService
    @Inject
    constructor() {
    fun homeTimeLine(timelineRequest: TimelineRequest): ResLiveData<List<HomeTimelineTweet>> {
//        return OkGo.get<List<HomeTimelineTweet>>(TwitterUrl.HOME_TIMELINE)
//                .params(timelineRequest)
        TODO()
    }
}