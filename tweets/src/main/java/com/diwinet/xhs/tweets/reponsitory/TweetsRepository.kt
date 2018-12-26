package com.diwinet.xhs.tweets.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.dataResource.Resource
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.webservice.TweetsWebService
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/9/28.
 */
class TweetsRepository
    @Inject
    constructor(
            private val tweetsWebService: TweetsWebService
    ) {

    fun homeTimeline(): LiveData<Resource<List<HomeTimelineTweet>>> {
//        return tweetsWebService.homeTimeLine(TimelineRequest())
//                .toJsonLiveData()
        TODO()
    }
}