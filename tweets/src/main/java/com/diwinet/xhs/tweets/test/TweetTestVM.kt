package com.diwinet.xhs.tweets.test

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.basic.BaseVM
import com.dbscarlet.common.dataResource.Resource
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.reponsitory.TweetsRespository
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/9/28.
 */
class TweetTestVM
    @Inject
    constructor(
            private val tweetsRepository: TweetsRespository
    ) :BaseVM() {

    fun homeTimeline(): LiveData<Resource<HomeTimelineTweet>> {
        return tweetsRepository.homeTimeline()
    }
}