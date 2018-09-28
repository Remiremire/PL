package com.diwinet.xhs.tweets.di

import com.diwinet.xhs.tweets.test.TweetTestAct
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Daibing Wang on 2018/9/28.
 */
@Module
abstract class TweetInjectors {
    @ContributesAndroidInjector
    abstract fun tweetTest() : TweetTestAct
}