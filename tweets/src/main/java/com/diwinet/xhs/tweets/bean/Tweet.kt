package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/26.
 */
open class Tweet{
    lateinit var created_at: String
    lateinit var id_str: String
    lateinit var text: String
    lateinit var source: String
    var truncated: Boolean = false
    var in_reply_to_status_id_str: String? = null
    var in_reply_to_user_id_str: String? = null
    var in_reply_to_screen_name: String? = null
    lateinit var user: User
    var place: Place? = null
    var quoted_status_id_str: String? = null
    var is_quote_status: Boolean? = null
    var quoted_status: Tweet? = null
    var retweeted_status: Tweet? = null
    var quote_count: Int = 0
    var reply_count: Int = 0
    var retweet_count: Int = 0
    var favorite_count: Int = 0
    lateinit var entities: Entities
    var extended_entities: ExEntities? = null
    var favorited: Boolean? = null
    var retweeted: Boolean = false
    var possibly_sensitive: Boolean? = null
    var filter_level: String? = null
    var lang: String? = null
    var matching_rules: List<MatchingRule>? = null

    class Entities {
        lateinit var hashtags: List<Hashtag>
        lateinit var symbols: List<Symbol>
        lateinit var user_mentions: List<UserMention>
        lateinit var urls: List<Url>
        var media: List<Media>? = null
    }

    class ExEntities{
        lateinit var media: List<ExMedia>
    }

    class MatchingRule{
        lateinit var tag: String
        lateinit var id_str: String
    }
}