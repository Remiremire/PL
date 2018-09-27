package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/26.
 */
open class Tweet{
    val created_at: String = ""
    val id_str: String = ""
    val text: String = ""
    val source: String = ""
    val truncated: Boolean = false
    val in_reply_to_status_id_str: String? = null
    val in_reply_to_user_id_str: String? = null
    val in_reply_to_screen_name: String? = null
    val user: User = User()
    val place: Place? = null
    val quoted_status_id_str: String? = null
    val is_quote_status: Boolean? = null
    val quoted_status: Tweet? = null
    val retweeted_status: Tweet? = null
    val quote_count: Int = 0
    val reply_count: Int = 0
    val retweet_count: Int = 0
    val favorite_count: Int = 0
    val entities: Entities = Entities()
    val extendedEntities: ExEntities? = ExEntities()
    val favorited: Boolean? = null
    val retweeted: Boolean = false
    val possibly_sensitive: Boolean? = null
    val filter_level: String? = null
    val lang: String? = null
    val matching_rules: List<MatchingRule>? = null

    class Entities() {

    }

    class ExEntities() {

    }

    data class MatchingRule(
        val tag: String,
        val id_str: String
    )
}