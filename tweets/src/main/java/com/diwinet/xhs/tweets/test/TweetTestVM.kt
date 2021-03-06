package com.diwinet.xhs.tweets.test

import android.arch.lifecycle.LiveData
import com.dbscarlet.common.basic.BaseVM
import com.dbscarlet.common.dataResource.Resource
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.reponsitory.TweetsRepository
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/9/28.
 */
class TweetTestVM
    @Inject
    constructor(
            private val tweetsRepository: TweetsRepository
    ) :BaseVM() {

    fun homeTimeline(): LiveData<Resource<List<HomeTimelineTweet>>> {
        return tweetsRepository.homeTimeline()
    }
}