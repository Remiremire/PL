package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/20.
 */

class User(
    val id_str: String,
    val name: String,
    val screen_name: String,
    val location: String?,
    val url: String?,
    val description: String?,
    val derived: Any,
    val protected: Boolean,
    val verified: Boolean,
    val followers_count: Int,
    val friends_count: Int,
    val listed_count: Int,
    val favourites_count: Int,
    val statuses_count: Int,
    val created_at: String,
    val geo_enabled: Boolean,
    val lang: String,
    val contributors_enabled: Boolean,
    val profile_background_color: String,
    val profile_background_image_url: String,
    val profile_background_image_url_https: String,
    val profile_background_tile: Boolean,
    val profile_banner_url: String,
    val profile_image_url: String,
    val profile_image_url_https: String,
    val profile_link_color: String,
    val profile_sidebar_border_color: String,
    val profile_sidebar_fill_color: String,
    val profile_text_color: String,
    val profile_use_background_image: String,
    val default_profile: Boolean,
    val default_profile_image: Boolean,
    val withheld_in_countries: List<String>,
    val withheld_scope: String
) {
    class Entities(
            val url: UrlInfo,
            val description: Any
    ) {
        class UrlInfo(
                val urls: List<Url>
        )
    }

}



