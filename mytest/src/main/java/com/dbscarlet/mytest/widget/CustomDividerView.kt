package com.dbscarlet.mytest.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.dbscarlet.mytest.R

/**
 * Created by Daibing Wang on 2019/3/29.
 */
class CustomDividerView: View {
    private val TYPE_LINE = 0
    private val TYPE_IMAGINARY = 1
    private val TYPE_DOTS = 2

    private val HORIZONTAL = 0
    private val VERTICAL = 1

    private var dvdOrientation = 0
    private var dvdType = 0
    private var dvdCount = 1
        get() {
            return if (field > 1) field else 1
        }
    private var dvdSize = 0
    private var dvdInSize = 0
    private var dvdInterval = 0
    private var dvdInInterval = 0
    private var dvdColor = 0
    private val paint = Paint()

    constructor(context: Context): this(context, null)
    constructor(context: Context, attr: AttributeSet?): this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int): super(context, attr, defStyleAttr) {
        initDivider(context, attr, defStyleAttr, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ): super(context, attrs, defStyleAttr, defStyleRes) {
        initDivider(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initDivider(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        paint.isAntiAlias = true
        paint.flags = Paint.ANTI_ALIAS_FLAG

        attrs ?: return
        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomDividerView,
            defStyleAttr,
            defStyleRes
        )
        dvdOrientation = attributes.getInt(R.styleable.CustomDividerView_dvdOrientation, HORIZONTAL)
        dvdType = attributes.getInt(R.styleable.CustomDividerView_dvdType, TYPE_LINE)

        dvdCount = attributes.getInt(R.styleable.CustomDividerView_dvdCount, 1)
        dvdInterval = attributes.getDimensionPixelSize(R.styleable.CustomDividerView_dvdInterval, dvdSize)

        dvdSize = attributes.getDimensionPixelSize(R.styleable.CustomDividerView_dvdSize, 0)
        dvdInSize = attributes.getDimensionPixelSize(R.styleable.CustomDividerView_dvdInSize, dvdSize)
        dvdInInterval = attributes.getDimensionPixelSize(R.styleable.CustomDividerView_dvdInInterval, dvdInSize)
        dvdColor = attributes.getColor(R.styleable.CustomDividerView_dvdColor, 0)
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wSize = MeasureSpec.getSize(widthMeasureSpec)
        var wMode = MeasureSpec.getMode(widthMeasureSpec)
        var hSize = MeasureSpec.getSize(heightMeasureSpec)
        var hMode = MeasureSpec.getMode(heightMeasureSpec)
        if (dvdOrientation == HORIZONTAL && hMode != MeasureSpec.EXACTLY) {
            hSize = paddingTop + paddingBottom + (dvdSize + dvdInterval) * dvdCount - dvdInterval
            hMode = MeasureSpec.EXACTLY
        }
        if (dvdOrientation == VERTICAL && hMode != MeasureSpec.EXACTLY) {
            wSize = paddingLeft + paddingRight + (dvdSize + dvdInterval) * dvdCount - dvdInterval
            wMode = MeasureSpec.EXACTLY
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(wSize, wMode),
            MeasureSpec.makeMeasureSpec(hSize, hMode))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val count = dvdCount
        when(dvdOrientation) {
            HORIZONTAL->{
                for (i in 0 until count) {
                    val cY = paddingTop + i * (dvdSize + dvdInterval) + dvdSize.toFloat() / 2
                    when(dvdType) {
                        TYPE_LINE -> {
                            drawLineH(canvas, cY)
                        }
                        TYPE_IMAGINARY -> {
                            drawImaginaryH(canvas, cY)
                        }
                        TYPE_DOTS -> {
                            drawDotsH(canvas, cY)
                        }
                    }
                }
            }
            VERTICAL->{
                for (i in 0 until count) {
                    val cx = paddingLeft + i * (dvdSize + dvdInterval) + dvdSize.toFloat() / 2
                    when(dvdType) {
                        TYPE_LINE -> {
                            drawLineV(canvas, cx)
                        }
                        TYPE_IMAGINARY -> {
                            drawImaginaryV(canvas, cx)
                        }
                        TYPE_DOTS -> {
                            drawDotsV(canvas, cx)
                        }
                    }
                }
            }
        }
    }

    private fun drawLineH(canvas: Canvas, cY: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = dvdColor
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeWidth = dvdSize.toFloat()

        canvas.drawLine(paddingLeft.toFloat(), cY, width.toFloat() - paddingRight, cY, paint)
    }

    private fun drawLineV(canvas: Canvas, cx: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = dvdColor
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeWidth = dvdSize.toFloat()

        canvas.drawLine(cx, paddingTop.toFloat(), cx, height.toFloat() - paddingBottom, paint)
    }

    private fun drawImaginaryH(canvas: Canvas, cy: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = dvdColor
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeWidth = dvdSize.toFloat()

        val len = width - paddingLeft - paddingRight
        val count = (len + dvdInInterval) / (dvdInSize + dvdInInterval)
        val interval = if (count > 1) {
            (len.toFloat() - dvdInSize * count) / (count - 1)
        } else {
            0F
        }
        var start = paddingLeft.toFloat()
        for(i in 0 until count) {
            canvas.drawLine(start, cy, start + dvdInSize, cy, paint)
            start += dvdInSize + interval
        }
    }

    private fun drawImaginaryV(canvas: Canvas, cx: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = dvdColor
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeWidth = dvdSize.toFloat()

        val len = height - paddingTop - paddingBottom
        val count = (len + dvdInInterval) / (dvdInSize + dvdInInterval)
        val interval = if (count > 1) {
            (len.toFloat() - dvdInSize * count) / (count - 1)
        } else {
            0F
        }
        var start = paddingTop.toFloat()
        for(i in 0 until count) {
            canvas.drawLine(cx, start, cx, start + dvdInSize, paint)
            start += dvdInSize + interval
        }
    }

    private fun drawDotsH(canvas: Canvas, cy: Float) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0F
        paint.color = dvdColor

        val radius = dvdSize.toFloat() / 2
        val len = width - paddingLeft - paddingRight
        val count = ((len + dvdInInterval) / (radius * 2 + dvdInInterval)).toInt()
        val interval = if (count > 1) {
            (len.toFloat() - radius * 2 * count) / (count - 1)
        } else {
            0F
        }
        var center = paddingLeft + radius
        for(i in 0 until count) {
            canvas.drawCircle(center, cy, radius, paint)
            center += radius * 2 + interval
        }
    }

    private fun drawDotsV(canvas: Canvas, cx: Float) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0F
        paint.color = dvdColor

        val radius = dvdSize.toFloat() / 2
        val len = height - paddingTop - paddingBottom
        val count = ((len + dvdInInterval) / (radius * 2 + dvdInInterval)).toInt()
        val interval = if (count > 1) {
            (len.toFloat() - radius * 2 * count) / (count - 1)
        } else {
            0F
        }
        var center = paddingTop + radius
        for(i in 0 until count) {
            canvas.drawCircle(cx, center, radius, paint)
            center += radius * 2 + interval
        }
    }

}