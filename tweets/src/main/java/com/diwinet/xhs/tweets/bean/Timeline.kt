package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/17.
 */
open class TimelineRequest(
        val count: Int?,
        val since_id: String?,
        val max_id: String?,
        val trim_user: Boolean?,
        val exclude_replies: Boolean?,
        val include_entities: Boolean?
)