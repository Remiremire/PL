package com.diwinet.xhs.tweets.bean

/**
 * Created by Daibing Wang on 2018/9/26.
 */
data class Place(
        val id: String,
        val url: String,
        val place_type: String,
        val name: String,
        val full_name: String,
        val country_code: String,
        val country: String,
        val bounding_box: BoundingBox,
        val attributes: Any
) {
    data class BoundingBox(
            val type: String,
            val coordinates: List<List<List<Double>>>
    )
}

