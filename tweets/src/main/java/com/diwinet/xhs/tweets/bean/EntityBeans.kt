package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/12.
 */

class Hashtag{
    lateinit var indices: List<Int>
    lateinit var text: String
}

class Url{
    lateinit var indices: List<Int>
    lateinit var url: String
    lateinit var display_url: String
    lateinit var expanded_url: String
    lateinit var unwound: Unwound

    class Unwound {
        lateinit var url: String
        var status: Int = 0
        lateinit var title: String
        lateinit var description: String
    }
}

class UserMention{
    lateinit var name: String
    lateinit var indices: List<Int>
    lateinit var screen_name: String
    lateinit var id_str: String
}

class Symbol{
    lateinit var indices: List<Int>
    lateinit var text: String
}

class Poll{
    lateinit var options: List<Option>
    lateinit var end_datetime: String
    var duration_minutes: Int = 0

    class Option{
        var position: Int = 0
        lateinit var text: String
    }
}

class Media {
    lateinit var type: String
    lateinit var sizes: Sizes
    lateinit var indices: List<Int>
    lateinit var url: String
    lateinit var media_url: String
    lateinit var display_url: String
    lateinit var id_str: String
    lateinit var expanded_url: String
    lateinit var media_url_https: String
    var source_status_id_str: String? = null
}

class Sizes{
    lateinit var thumb: SizeInfo
    lateinit var large: SizeInfo
    lateinit var medium: SizeInfo
    lateinit var small: SizeInfo

    class SizeInfo{
        var w: Int = 0
        var h: Int = 0
        lateinit var resize: String

    }
}

class ExMedia{
    lateinit var type: String
    lateinit var sizes: Sizes
    lateinit var indices: List<Int>
    lateinit var url: String
    lateinit var media_url: String
    lateinit var display_url: String
    lateinit var id_str: String
    lateinit var expanded_url: String
    lateinit var media_url_https: String
    var source_status_id_str: String? = null
    var video_info: VideoInfo? = null
    var additional_media_info: AdditionalMediaInfo? = null
}

class VideoInfo{
    lateinit var aspect_ratio: List<Int>
    var duration_millis: Int = 0
    lateinit var variants: List<Variant>

    class Variant{
        lateinit var content_type: String
        lateinit var url: String
        var bitrate: Int = 0
    }
}

class AdditionalMediaInfo{
    var monetizable: Boolean = false
}