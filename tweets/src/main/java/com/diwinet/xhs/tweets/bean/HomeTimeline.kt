package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/17.
 */

data class HomeTimeline(
    val coordinates: Any,
    val truncated: Boolean,
    val created_at: String,
    val favorited: Boolean,
    val id_str: String,
    val in_reply_to_user_id_str: Any,
    val entities: Entities,
    val text: String,
    val contributors: Any,
    val id: Long,
    val retweet_count: Int,
    val in_reply_to_status_id_str: Any,
    val geo: Any,
    val retweeted: Boolean,
    val in_reply_to_user_id: Any,
    val place: Any,
    val source: String,
    val user: TweetUser,
    val in_reply_to_screen_name: Any,
    val in_reply_to_status_id: Any
)