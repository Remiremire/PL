package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/17.
 */
open class TimelineRequest{
    val count: Int? = null
    val since_id: String? = null
    val max_id: String? = null
    val trim_user: Boolean? = null
    val exclude_replies: Boolean? = null
    val include_entities: Boolean? = null
}

class HomeTimelineTweet : Tweet() {

}