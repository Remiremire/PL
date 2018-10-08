package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/20.
 */
open class User{
    lateinit var id_str: String
    lateinit var name: String
    lateinit var screen_name: String
    var location: String? = null
    var url: String? = null
    var description: String? = null
    lateinit var derived: Any
    var protected: Boolean = false
    var verified: Boolean = false
    var followers_count: Int = 0
    var friends_count: Int = 0
    var listed_count: Int = 0
    var favourites_count: Int = 0
    var statuses_count: Int = 0
    lateinit var created_at: String
    var geo_enabled: Boolean = false
    lateinit var lang: String
    var contributors_enabled: Boolean = false
    lateinit var profile_background_color: String
    lateinit var profile_background_image_url: String
    lateinit var profile_background_image_url_https: String
    var profile_background_tile: Boolean = false
    lateinit var profile_banner_url: String
    lateinit var profile_image_url: String
    lateinit var profile_image_url_https: String
    lateinit var profile_link_color: String
    lateinit var profile_sidebar_border_color: String
    lateinit var profile_sidebar_fill_color: String
    lateinit var profile_text_color: String
    lateinit var profile_use_background_image: String
    var default_profile: Boolean = false
    var default_profile_image: Boolean = false
    lateinit var withheld_in_countries: List<String>
    lateinit var withheld_scope: String

    class Entities{
        lateinit var url: UrlInfo
        lateinit var description: Any
        class UrlInfo {
            lateinit var urls: List<Url>
        }
    }

}



