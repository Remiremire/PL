package com.diwinet.xhs.tweets.test

import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.dataResource.ResObserver
import com.dbscarlet.common.dataResource.Success
import com.dbscarlet.common.commonUtil.logI
import com.diwinet.xhs.tweets.R
import com.diwinet.xhs.tweets.bean.HomeTimelineTweet
import com.diwinet.xhs.tweets.databinding.ActTweetTestBinding
import javax.inject.Inject

/**
 * Created by Daibing Wang on 2018/9/28.
 */
@Route(path = ActPath.Tweet.TWEET_TEST)
class TweetTestAct: BaseActivity<ActTweetTestBinding>() {
    @Inject
    lateinit var tweetTestVM: TweetTestVM

    override fun getContentLayout(): Int {
        return R.layout.act_tweet_test
    }

    override fun initView() {
        binding.btnHomeTimeline
                .setOnClickListener {
                    tweetTestVM.homeTimeline()
                            .observe(this, object : ResObserver<List<HomeTimelineTweet>>(){
                                override fun onSuccess(res: Success<List<HomeTimelineTweet>>) {
                                    logI(res.data)
                                }
                            })
                }
    }
}