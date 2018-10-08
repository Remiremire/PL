package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/26.
 */
class Place{
    lateinit var id: String
    lateinit var url: String
    lateinit var place_type: String
    lateinit var name: String
    lateinit var full_name: String
    lateinit var country_code: String
    lateinit var country: String
    lateinit var bounding_box: BoundingBox
    lateinit var attributes: Any

    class BoundingBox{
        lateinit var type: String
        lateinit var coordinates: List<List<List<Double>>>
    }
}

