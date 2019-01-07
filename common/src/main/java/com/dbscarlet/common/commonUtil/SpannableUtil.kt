package com.dbscarlet.common.commonUtil

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*

/**
 * Created by Daibing Wang on 2018/12/3.
 */

/**
 * 字符串转换为[SpannableString]
 * [spanSet]字符属性设置
 */
fun CharSequence?.span(spanSet: (SpannableNode.() -> Unit)? = null): SpannableBuilder {
    return SpannableBuilder().with(this, spanSet)
}

/**
 * 连接一个字符串，并转换为[SpannableString]
 * [nextSpanSet]下个字符属性设置
 */
fun CharSequence?.with(nextText: CharSequence?, nextSpanSet: (SpannableNode.() -> Unit)?): SpannableBuilder {
    return span().with(nextText, nextSpanSet)
}

class SpannableNode(
        val text: CharSequence?
) {
    /**
     * 字体大小
     */
    var absoluteSizeSpan: AbsoluteSizeSpan? = null
    /**
     * 字体大小
     */
    fun absoluteSize(size: Int?) {
        absoluteSizeSpan = if (size == null) {
            null
        } else {
            AbsoluteSizeSpan(size)
        }
    }

    /**
     * 相对字体大小
     */
    var relativeSizeSpan: RelativeSizeSpan? = null
    /**
     * 相对字体大小
     */
    fun relativeSize(size: Float?) {
        relativeSizeSpan = if (size == null) {
            null
        } else {
            RelativeSizeSpan(size)
        }
    }

    /**
     * 字体颜色
     */
    var textColorSpan: ForegroundColorSpan? = null
    /**
     * 字体颜色
     */
    fun textColor(color: Int?) {
        textColorSpan = if (color == null) {
            null
        } else {
            ForegroundColorSpan(color)
        }
    }

    /**
     * 背景色
     */
    var bgColorSpan: BackgroundColorSpan? = null
    /**
     * 背景色
     */
    fun bgColor(color: Int?) {
        bgColorSpan = if (color == null) {
            null
        } else {
            BackgroundColorSpan(color)
        }
    }

    /**
     * 中划线
     */
    var centerLineSpan: StrikethroughSpan? = null
    /**
     * 中划线
     */
    fun centerLine(centerLine: Boolean) {
        centerLineSpan = if (centerLine) {
            StrikethroughSpan()
        } else {
            null
        }
    }

    /**
     * 下划线
     */
    var underLineSpan: UnderlineSpan? = null
    /**
     * 下划线
     */
    fun underLine(underLine: Boolean) {
        underLineSpan = if (underLine) {
            UnderlineSpan()
        } else {
            null
        }
    }

    /**
     * 文字设置为上标
     */
    var superscriptSpan: SuperscriptSpan? = null
    /**
     * 文字设置为上标
     */
    fun superscript(superscript: Boolean) {
        superscriptSpan = if (superscript) {
            SuperscriptSpan()
        } else {
            null
        }
    }

    /**
     * 文字设置为下标
     */
    var subscriptSpan: SubscriptSpan? = null
    /**
     * 文字设置为下标
     */
    fun subscript(subscript: Boolean) {
        subscriptSpan = if (subscript) {
            SubscriptSpan()
        } else {
            null
        }
    }

    /**
     * 文字风格
     */
    var styleSpan: StyleSpan? = null
    /**
     * 文字风格
     *  斜体[android.graphics.Typeface.ITALIC]
     *  粗体[android.graphics.Typeface.BOLD]
     */
    fun style(style: Int?) {
        styleSpan = if (style == null) {
            null
        } else {
            StyleSpan(style)
        }
    }

    /**
     * 将文字替换为图片, eg:各类表情(将 /gz /kb 这类字符替换为表情)
     */
    var imageSpan: ImageSpan? = null
    /**
     * 将文字替换为图片, eg:各类表情(将 /gz /kb 这类字符替换为表情)
     */
    fun image(drawable: Drawable?) {
        imageSpan = if (drawable == null) {
            null
        } else {
            ImageSpan(drawable)
        }
    }

    /**
     * 点击事件,需要设置[android.widget.TextView.setMovementMethod]
     */
    var clickSpan: ClickableSpan? =null

    /**
     * 设置超链接,需要设置[android.widget.TextView.setMovementMethod]
     */
    var urlSpan: URLSpan? = null
    /**
     * 设置超链接,需要设置[android.widget.TextView.setMovementMethod]
     */
    fun url(url: String?) {
        urlSpan = if (url == null) {
            null
        } else {
            URLSpan(url)
        }
    }
}

class SpannableBuilder {
    private val nodeList = mutableListOf<SpannableNode>()

    /**
     * 增加一个SpannableString的节点
     * [text]新增节点的字符
     * [spanSet]字符属性设置
     */
    fun with(text: CharSequence?, spanSet: (SpannableNode.() -> Unit)? = null): SpannableBuilder {
        val node = SpannableNode(text)
        nodeList.add(node)
        spanSet?.invoke(node)
        return this
    }

    /**
     * 创建[SpannableString]
     */
    fun build() : SpannableString{
        var string = ""
        nodeList.forEach {
            val text = it.text ?: return@forEach
            string += text
        }

        val result = SpannableString(string)
        if (result.isEmpty()) return result

        var index = 0
        var start = 0
        var end = 0
        fun applySpan(span: Any?) {
            if (span != null) {
                result.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        nodeList.forEach { node ->
            start = index
            end = start + (node.text?.length ?: 0)
            index = end

            if (start == end) return@forEach

            applySpan(node.absoluteSizeSpan)
            applySpan(node.relativeSizeSpan)
            applySpan(node.textColorSpan)
            applySpan(node.bgColorSpan)
            applySpan(node.centerLineSpan)
            applySpan(node.underLineSpan)
            applySpan(node.superscriptSpan)
            applySpan(node.subscriptSpan)
            applySpan(node.styleSpan)
            applySpan(node.imageSpan)
            applySpan(node.clickSpan)
            applySpan(node.urlSpan)
        }

        return result
    }
}

