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
    val expanded_url: String?,
    val url: String,
    val indices: List<Int>,
    val display_url: String?
)

data class TweetUser(
    val id: Int,
    val id_str: String,
    val name: String,
    val screen_name: String,
    val location: String,
    val description: String,
    val url: String,
    val entities: Entities,
    val protected: Boolean,
    val followers_count: Int,
    val friends_count: Int,
    val listed_count: Int,
    val created_at: String,
    val favourites_count: Int,
    val utc_offset: Int,
    val time_zone: String,
    val geo_enabled: Boolean,
    val verified: Boolean,
    val statuses_count: Int,
    val lang: String,
    val contributors_enabled: Boolean,
    val is_translator: Boolean,
    val is_translation_enabled: Boolean,
    val profile_background_color: String,
    val profile_background_image_url: String,
    val profile_background_image_url_https: String,
    val profile_background_tile: Boolean,
    val profile_image_url: String,
    val profile_image_url_https: String,
    val profile_banner_url: String,
    val profile_link_color: String,
    val profile_sidebar_border_color: String,
    val profile_sidebar_fill_color: String,
    val profile_text_color: String,
    val profile_use_background_image: Boolean,
    val has_extended_profile: Boolean,
    val default_profile: Boolean,
    val default_profile_image: Boolean,
    val following: Boolean,
    val follow_request_sent: Boolean,
    val notifications: Boolean,
    val translator_type: String
)

data class Entities(
        val url: EntitiesUrl,
        val description: Description,
        val hashtags: List<Any>,
        val user_mentions: List<UserMention>
) {
    data class Description(
        val urls: List<Url>
    )
    data class EntitiesUrl(
        val urls: List<Url>
    )
}
