package com.dbscarlet.mediademo.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * &#6700
 * Created by Daibing Wang on 2018/11/30.
 */
class ImageClipView : View {
    private var imageBmp: Bitmap? = null
    private val paint: Paint = Paint()
    private var shear: Shear? = null

    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ): super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ): super(context, attrs, defStyleAttr, defStyleRes)

    init {
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    fun clip(): Bitmap? {
        val bmp = imageBmp ?: return null
        val clipPath = shear?.clipResult() ?: return bmp

        val resultBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBmp)
        canvas.clipPath(clipPath, Region.Op.INTERSECT)
        canvas.drawBitmap(bmp, 0f, 0f, paint)
        return resultBmp
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return shear?.onClipViewTouch(this, event) ?: super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bmp = imageBmp ?: return
        canvas.drawBitmap(bmp, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
        paddingBottom
        val clipPath = shear?.clipResult() ?: return
        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
        canvas.drawColor(Color.argb((0.5 * 255 + 0.5).toInt() , 0, 0, 0))
    }

    interface Shear {
        fun onClipViewSizeChange(view: View)
        fun clipResult(): Path?
        fun onClipViewTouch(view: View, event: MotionEvent): Boolean
    }

    class RectShear: Shear {
        private var rectF: RectF? = null
        override fun onClipViewSizeChange(view: View) {
            rectF = RectF(view.paddingLeft.toFloat(),
                    view.paddingTop.toFloat(),
                    (view.width - view.paddingRight).toFloat(),
                    (view.height - view.paddingBottom).toFloat())

        }

        override fun clipResult(): Path? {
            return null
        }

        override fun onClipViewTouch(view: View, event: MotionEvent): Boolean {
            return false
        }

    }
}