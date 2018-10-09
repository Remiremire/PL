package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/17.
 */
open class TimelineRequest{
    var count: Int? = null
    var since_id: String? = null
    var max_id: String? = null
    var trim_user: Boolean? = null
    var exclude_replies: Boolean? = null
    var include_entities: Boolean? = null
}

class HomeTimelineTweet : Tweet() {

}