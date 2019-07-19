package com.dbscarlet.mytest.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.dbscarlet.mytest.R
import kotlin.math.min

/**
 * Created by Daibing Wang on 2019/4/29.
 */
class ClipLayout: FrameLayout {
    companion object {
        const val CLIP_NONE = 0
        const val CLIP_CIRCLE = 1
        const val CLIP_CORNERS = 2
    }
    private var clType: Int = CLIP_NONE
    private var strokeWidth = 0
    private var strokeColor = 0
    private var radiusLT = 0
    private var radiusRT = 0
    private var radiusRB = 0
    private var radiusLB = 0
    private val mPaint = Paint()
    private val path = Path()

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ): super(context, attrs, defStyleAttr) {
        initClipFromAttr(context, attrs, defStyleAttr, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ): super(context, attrs, defStyleAttr, defStyleRes) {
        initClipFromAttr(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initClipFromAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        mPaint.isAntiAlias = true
        attrs ?: return
        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.ClipLayout,
            defStyleAttr,
            defStyleRes
        )
        clType = attributes.getInt(R.styleable.ClipLayout_clType, CLIP_NONE)
        strokeWidth = attributes.getDimensionPixelSize(R.styleable.ClipLayout_clStrokeWidth, 0)
        strokeColor = attributes.getColor(R.styleable.ClipLayout_clStrokeColor, 0)
        radiusLT = attributes.getDimensionPixelSize(R.styleable.ClipLayout_clLT, 0)
        radiusRT = attributes.getDimensionPixelSize(R.styleable.ClipLayout_clRT, 0)
        radiusRB = attributes.getDimensionPixelSize(R.styleable.ClipLayout_clRB, 0)
        radiusLB = attributes.getDimensionPixelSize(R.styleable.ClipLayout_clLB, 0)
        attributes.recycle()
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val result: Boolean
        if (canvas != null && clType != CLIP_NONE) {
            canvas.save()
            canvas.clipPath(path)
            result = super.drawChild(canvas, child, drawingTime)
            canvas.restore()

            if (strokeWidth > 0) {
                mPaint.style = Paint.Style.STROKE
                mPaint.flags = Paint.ANTI_ALIAS_FLAG
                mPaint.color = strokeColor
                mPaint.strokeWidth = strokeWidth.toFloat()
                mPaint.strokeCap = Paint.Cap.ROUND

                canvas.drawPath(path, mPaint)
            }
        } else {
            result = super.drawChild(canvas, child, drawingTime)
        }
        return result
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        clipPath()
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun clipPath() {
        path.reset()
        val l = paddingLeft.toFloat() + strokeWidth
        val r = (width - paddingRight).toFloat() - strokeWidth
        val t = paddingTop.toFloat() + strokeWidth
        val b = (height - paddingBottom).toFloat() - strokeWidth
        val w = r - l
        val h = b - t
        when(clType) {
            CLIP_CIRCLE -> {
                path.moveTo(l + w / 2, t)
                path.addCircle(l + w / 2, t + h / 2, min(w, h) / 2, Path.Direction.CW)
                path.close()
            }
            CLIP_CORNERS -> {
                path.moveTo(l + radiusLT, t)

                path.lineTo(r - radiusRT, t)
                path.arcTo(RectF(r - radiusRT * 2, t, r, t + radiusRT * 2), 270f, 90f)

                path.lineTo(r, b - radiusRB)
                path.arcTo(RectF(r - radiusRB * 2, b - radiusRB * 2, r, b), 0f, 90f)

                path.lineTo(l + radiusLB, b)
                path.arcTo(RectF(l, b - radiusLB * 2, l + radiusLB * 2, b), 90f, 90f)

                path.lineTo(l, t + radiusLT)
                path.arcTo(RectF(l, t, l + radiusLT * 2, t + radiusLT * 2), 180f, 90f)

                path.close()
            }
        }
    }
}