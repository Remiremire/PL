package com.dbscarlet.applib.twitterNetwork

/**
 * Created by Daibing Wang on 2018/8/17.
 */
const val CONSUMER_SECRET = "KoTFoWKEuDALjbxx0qmArc4qdhRATaY2bCuKfQuLqOhnSK6Jqr"
const val CONSUMER_KEY = "PUL25DBIvY54psfJ5Mqhwvwc2"
const val SIGNATURE_METHOD = "HMAC-SHA1"
const val OAUTH_VERSION = "1.0"

const val DEF_OAUTH_TOKEN = "744155165423509505-SypL6sBZhGM0AZi3v2udHeY4f6Dd95r"
const val DEF_OAUTH_TOKEN_SECRET = "mPDOh1dtvFcmQYLHRqrWKBG53GbcPkwyWZuvN9wCtzEog"

var OAUTH_TOKEN: String? = null
    private set
var OAUTH_TOKEN_SECRET: String? = null
    private set

fun setToken(token: String, secret: String) {
    OAUTH_TOKEN = token
    OAUTH_TOKEN_SECRET = secret
}

fun cleanToken() {
    OAUTH_TOKEN = null
    OAUTH_TOKEN_SECRET = null
}

/**
 * 如果请求时添加了这个header，将使用这个token
 * 需要和 HEADER_OAUTH_SECRET 一起添加才有效
 */
const val HEADER_OAUTH_TOKEN = "oauth_token"
/**
 * 如果请求时添加了这个header，将使用这个token secret
 * 需要和 HEADER_OAUTH_TOKEN 一起添加才有效
 */
const val HEADER_OAUTH_SECRET = "oauth_token_secret"