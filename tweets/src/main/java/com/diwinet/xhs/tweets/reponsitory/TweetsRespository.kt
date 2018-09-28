package com.diwinet.xhs.tweets.reponsitory

import android.arch.lifecycle.LiveData
import com.dbscarlet.applib.twitterNetwork.toJsonLiveData
import com.dbscarlet.common.dataResource.Resource
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.webservice.TweetsWebService
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/9/28.
 */
class TweetsRespository
    @Inject
    constructor(
            private val tweetsWebService: TweetsWebService
    ) {

    fun homeTimeline(): LiveData<Resource<HomeTimelineTweet>> {
        return tweetsWebService.homeTimeLine()
                .toJsonLiveData()
    }
}