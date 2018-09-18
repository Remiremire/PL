package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/12.
 */

data class Hashtag(
    val indices: List<Int>,
    val text: String
)

data class Url(
    val indices: List<Int>,
    val url: String,
    val display_url: String,
    val expanded_url: String,
    val unwound: Unwound?
) {

    data class Unwound(
        val url: String,
        val status: Int,
        val title: String,
        val description: String
    )
}

data class UserMention(
    val name: String,
    val indices: List<Int>,
    val screen_name: String,
    val id_str: String
)

data class Symbol(
    val indices: List<Int>,
    val text: String
)

data class Poll(
    val options: List<Option>,
    val end_datetime: String,
    val duration_minutes: Int
) {

    data class Option(
            val position: Int,
            val text: String
    )
}

data class Media(
    val type: String,
    val sizes: Sizes,
    val indices: List<Int>,
    val url: String,
    val media_url: String,
    val display_url: String,
    val id_str: String,
    val expanded_url: String,
    val media_url_https: String,
    val source_status_id_str: String?
) {

    data class Sizes(
            val thumb: Thumb,
            val large: Large,
            val medium: Medium,
            val small: Small
    )

    data class Large(
            val h: Int,
            val resize: String,
            val w: Int
    )

    data class Small(
            val h: Int,
            val resize: String,
            val w: Int
    )

    data class Medium(
            val h: Int,
            val resize: String,
            val w: Int
    )

    data class Thumb(
            val h: Int,
            val resize: String,
            val w: Int
    )
}

