package com.diwinet.xhs.tweets.webservice

/**
 * Created by Daibing Wang on 2018/8/27.
 */

data class TimelineWebService(
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
    val user: User,
    val in_reply_to_screen_name: Any,
    val in_reply_to_status_id: Any
)

data class User(
    val name: String,
    val profile_sidebar_fill_color: String,
    val profile_background_tile: Boolean,
    val profile_sidebar_border_color: String,
    val profile_image_url: String,
    val created_at: String,
    val location: String,
    val follow_request_sent: Boolean,
    val id_str: String,
    val is_translator: Boolean,
    val profile_link_color: String,
    val entities: Entities,
    val default_profile: Boolean,
    val url: String,
    val contributors_enabled: Boolean,
    val favourites_count: Int,
    val utc_offset: Any,
    val profile_image_url_https: String,
    val id: Int,
    val listed_count: Int,
    val profile_use_background_image: Boolean,
    val profile_text_color: String,
    val followers_count: Int,
    val lang: String,
    val protected: Boolean,
    val geo_enabled: Boolean,
    val notifications: Boolean,
    val description: String,
    val profile_background_color: String,
    val verified: Boolean,
    val time_zone: Any,
    val profile_background_image_url_https: String,
    val statuses_count: Int,
    val profile_background_image_url: String,
    val default_profile_image: Boolean,
    val friends_count: Int,
    val following: Boolean,
    val show_all_inline_media: Boolean,
    val screen_name: String
)

data class Url(
    val expanded_url: Any,
    val url: String,
    val indices: List<Int>,
    val display_url: Any
)

data class Entities(
    val urls: List<Any>,
    val hashtags: List<Any>,
    val user_mentions: List<Any>,
    val description: Any
)