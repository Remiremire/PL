package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/12.
 */
data class UserMention(
    val name: String,
    val id_str: String,
    val id: Int,
    val indices: List<Int>,
    val screen_name: String
)

data class Url(
    val expanded_url: String,
    val url: String,
    val indices: List<Int>,
    val display_url: String
)