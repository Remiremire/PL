package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/26.
 */
class Tweet(
    val created_at: String,
    val id_str: String,
    val text: String,
    val source: String,
    val truncated: Boolean,
    val in_reply_to_status_id_str: String?,
    val in_reply_to_user_id_str: String?,
    val in_reply_to_screen_name: String?,
    val user: User,
    val place: Place?,
    val quoted_status_id_str: String?,
    val is_quote_status: Boolean?,
    val quoted_status: Tweet?,
    val retweeted_status: Tweet?,
    val quote_count: Int,
    val reply_count: Int,
    val retweet_count: Int,
    val favorite_count: Int,
    val entities: Entities,
    val extendedEntities: ExEntities?,
    val favorited: Boolean?,
    val retweeted: Boolean,
    val possibly_sensitive: Boolean?,
    val filter_level: String?,
    val lang: String?,
    val matching_rules: List<MatchingRule>?
) {

    class Entities() {

    }

    class ExEntities() {

    }

    data class MatchingRule(
        val tag: String,
        val id_str: String
    )
}