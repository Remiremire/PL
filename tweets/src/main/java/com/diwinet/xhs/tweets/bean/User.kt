package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/20.
 */

open class User{
    val id_str: String = ""
    val name: String = ""
    val screen_name: String = ""
    val location: String? = null
    val url: String? = null
    val description: String? = null
    val derived: Any = Any()
    val protected: Boolean = false
    val verified: Boolean = false
    val followers_count: Int = 0
    val friends_count: Int = 0
    val listed_count: Int = 0
    val favourites_count: Int = 0
    val statuses_count: Int = 0
    val created_at: String = ""
    val geo_enabled: Boolean = false
    val lang: String = ""
    val contributors_enabled: Boolean = false
    val profile_background_color: String = ""
    val profile_background_image_url: String = ""
    val profile_background_image_url_https: String = ""
    val profile_background_tile: Boolean = false
    val profile_banner_url: String = ""
    val profile_image_url: String = ""
    val profile_image_url_https: String = ""
    val profile_link_color: String = ""
    val profile_sidebar_border_color: String = ""
    val profile_sidebar_fill_color: String = ""
    val profile_text_color: String = ""
    val profile_use_background_image: String = ""
    val default_profile: Boolean = false
    val default_profile_image: Boolean = false
    val withheld_in_countries: List<String> = listOf()
    val withheld_scope: String = ""

    class Entities(
            val url: UrlInfo,
            val description: Any
    ) {
        class UrlInfo(
                val urls: List<Url>
        )
    }

}



