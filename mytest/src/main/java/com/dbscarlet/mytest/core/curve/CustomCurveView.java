package com.dbscarlet.mytest.core.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by Daibing Wang on 2018/7/20.
 */
public class CustomCurveView extends View {
    //曲线平滑度
    private static final float LINE_SMOOTHNESS = 0.16f;
    private Path path;
    private Paint linePaint;
    private Paint textPaint;
    private GradientDrawable lineGradient;
    private int xAxesTextColor = 0xff82879b;
    private int xAxesTextSize = 14;
    private float paddingCurveToXAxes = 13.5f;
    private int mSelectDataIndex = -1;
    private List<Value> mValues;
    private DataTextFormat dataTextFormat;
    private ChartFrame mChartFrame = new ChartFrame();
    private int mLineColor = 0xff079dfe;
    private boolean showSelectData = false;
    private boolean canSelectDataShow = true;
    private OnDataSelectListener onDataSelectListener;


    public CustomCurveView(Context context) {
        super(context);
        initView();
    }

    public CustomCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        path = new Path();
        linePaint = new Paint();
        textPaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setAntiAlias(true);
        setTextSize(textPaint, xAxesTextSize);
        lineGradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x73079DFE, 0x00ffffff});
        lineGradient.setShape(GradientDrawable.RECTANGLE);
        lineGradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    public void setDataTextFormat(DataTextFormat dataTextFormat) {
        this.dataTextFormat = dataTextFormat;
        postInvalidate();
    }

    public void setOnDataSelectListener(OnDataSelectListener onDataSelectListener) {
        this.onDataSelectListener = onDataSelectListener;
    }

    public void setLineFillColor(int...colors){
        if (colors == null || colors.length == 0){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            lineGradient.setColors(colors);
        }
        postInvalidate();
    }

    public void setCanSelectDataShow(boolean canSelectDataShow) {
        this.canSelectDataShow = canSelectDataShow;
        postInvalidate();
    }

    public void selectData(int index) {
        if (mValues == null || index >= mValues.size() || index < 0 ) {
            mSelectDataIndex = -1;
        } else {
            showSelectData = !showSelectData || index != mSelectDataIndex;
            mSelectDataIndex = index;
        }
        postInvalidate();
        if (onDataSelectListener != null && mSelectDataIndex != -1) {
            post(new Runnable() {
                @Override
                public void run() {
                    onDataSelectListener.onDataSelect(mSelectDataIndex, canSelectDataShow && showSelectData);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                int touchIndex = mChartFrame.computeIndex(event.getX());
                selectData(touchIndex);
                break;
        }
        return true;
    }

    private void reMeasureCurve() {
        setTextSize(textPaint, xAxesTextSize);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        mChartFrame.setFrame(getPaddingLeft() + dip2Px(16),
                getWidth() - getPaddingRight() - dip2Px(16),
                getPaddingTop() + dip2Px(30),
                getHeight() - getPaddingBottom() - dip2Px(paddingCurveToXAxes) - (fm.bottom - fm.top));
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        reMeasureCurve();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mValues != null && mValues.size() > 0){
            drawCurve(canvas, mValues, linePaint);
        }
    }

    private void drawCurve(Canvas canvas, List<Value> valueList, Paint paint){
        path.reset();
        final int valueSize = valueList.size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;
        int minY = Integer.MAX_VALUE;
        int firstX = 0;
        int lastX = 0;
        Float selectX = null;
        Float selectY = null;
        String selectDataText = null;
        for (int valueIndex = 0; valueIndex < valueSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Value value = valueList.get(valueIndex);
                currentPointX = mChartFrame.computeX(valueIndex);
                currentPointY = mChartFrame.computeY(value.value);
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    Value value = valueList.get(valueIndex - 1);
                    previousPointX = mChartFrame.computeX(valueIndex - 1);
                    previousPointY = mChartFrame.computeY(value.value);
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    Value value = valueList.get(valueIndex - 2);
                    prePreviousPointX = mChartFrame.computeX(valueIndex - 2);
                    prePreviousPointY = mChartFrame.computeY(value.value);
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // nextPoint is always new one or it is equal currentPoint.
            if (valueIndex < valueSize - 1) {
                Value value = valueList.get(valueIndex + 1);
                nextPointX =  mChartFrame.computeX(valueIndex + 1);
                nextPointY = mChartFrame.computeY(value.value);
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (currentPointY < minY){
                minY = (int) currentPointY;
            }
            if (valueIndex == 0){
                firstX = (int) currentPointX;
            }
            if (valueIndex == valueSize - 1){
                lastX = (int) currentPointX;
            }
            if (valueIndex == 0) {
                // Move to start point.
                path.moveTo(currentPointX, currentPointY);
            } else {
                // Calculate control points.
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);

                /////////////////连续三个值相同认为是直线，重新修正控制点//////////////
                if (currentPointY == previousPointY && currentPointY == prePreviousPointY && valueIndex > 2){
                    secondControlPointY = currentPointY;
                }
                if (valueIndex > 3 && prePreviousPointY == previousPointY){
                    float prePrePreviousPointY = mChartFrame.computeY(valueList.get(valueIndex - 3).value);
                    if (prePreviousPointY == prePrePreviousPointY){
                        firstControlPointX = previousPointX + (currentPointX - previousPointX) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        firstControlPointY = previousPointY;
                    }
                }
                if (currentPointY == previousPointY && valueIndex > 0
                        && currentPointY == nextPointY && valueIndex + 1 < valueSize){
                    firstControlPointY = currentPointY;
                }
                if (valueIndex + 2 < valueSize && currentPointY == nextPointY){
                    float nextNextPointY = mChartFrame.computeY(valueList.get(valueIndex + 2).value);
                    if (nextNextPointY == nextPointY){
                        secondControlPointX = currentPointX - (currentPointX - previousPointX) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        secondControlPointY = currentPointY;
                    }
                }

                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            if (valueSize == 1){
                path.rMoveTo(-dip2Px(5),0);
                path.rLineTo(dip2Px(10), 0);
            }

            if (valueIndex == mSelectDataIndex && valueIndex < valueSize ){
                selectX = currentPointX;
                selectY = currentPointY;
                Value value = valueList.get(valueIndex);
                if (value != null){
                    if (dataTextFormat != null){
                        selectDataText = dataTextFormat.format(value.value, valueIndex);
                    } else {
                        selectDataText = "" + value.value;
                    }
                }
            }

            // Shift values by one back to prevent recalculation of values that have
            // been already calculated.
            if (valueList != null && valueIndex < valueList.size()){
                drawXAxes(canvas, valueList.get(valueIndex).key, currentPointX);
            }
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        prepareLinePaint(paint);
        setTextSize(textPaint, xAxesTextSize);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int curvePadding = (int) (getPaddingBottom() + fm.bottom - fm.top + dip2Px(paddingCurveToXAxes));
        int curveBottom = getHeight() - curvePadding;
        canvas.drawPath(path, paint);
        path.lineTo(lastX, curveBottom);
        path.lineTo(firstX, curveBottom);
        path.close();
        canvas.save();
        canvas.clipPath(path);
        lineGradient.setBounds(firstX, 0, lastX, curveBottom);
        lineGradient.draw(canvas);
        canvas.restore();
        if (canSelectDataShow && showSelectData && selectX != null && selectY != null){
            drawSelectDataPoint(canvas, selectDataText, selectX, selectY, curveBottom, path);
        }
    }

    private void drawXAxes(Canvas canvas, String text, float centerX) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        setTextSize(textPaint, xAxesTextSize);
        textPaint.setColor(xAxesTextColor);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textBaseLine = getHeight() - getPaddingBottom() - fm.bottom;
        float len = textPaint.measureText(text);
        float left = centerX - len / 2;
        if (left < 0){
            left = 0;
        } else if (left + len > getWidth()){
            left = getWidth();
        }
        canvas.drawText(text, left, textBaseLine, textPaint);
    }
    private void prepareLinePaint(Paint linePaint) {
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dip2Px(2));
        linePaint.setColor(mLineColor);
    }

    private void drawSelectDataPoint(Canvas canvas, String dataText, float selectX, float selectY, float curveBottom, Path path){
        path.reset();
        path.moveTo(selectX, selectY);
        path.addCircle(selectX, selectY, dip2Px(4), Path.Direction.CW);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(Color.WHITE);
        canvas.drawPath(path, linePaint);
        path.moveTo(selectX, selectY + dip2Px(4));
        path.lineTo(selectX, curveBottom);
        prepareLinePaint(linePaint);
        canvas.drawPath(path, linePaint);

        if (TextUtils.isEmpty(dataText)){
            return;
        }

        textPaint.setColor(Color.WHITE);
        setTextSize(textPaint, 10);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textH = fm.bottom - fm.top;
        float triangleH = dip2Px(4);
        float triangleHalfW = dip2Px(4.5f);
        float text2bg = dip2Px(1);
        float radiuControl = dip2Px(10);
        float radiu = dip2Px(4);
        float textW = textPaint.measureText(dataText);
        float left;
        if (selectX - textW / 2 - radiu - getPaddingLeft() < 0){
            left = getPaddingLeft() + radiu;
        } else if (selectX + textW / 2 + radiu > getWidth() - getPaddingRight()) {
            left = getWidth() - getPaddingRight() - radiu - textW;
        } else {
            left = selectX - textW / 2;
        }

        path.reset();
        path.moveTo(selectX, selectY);
        path.rMoveTo(0, - dip2Px(8));
        path.rLineTo(- triangleHalfW, -triangleH);
        path.rLineTo(-(selectX - left - triangleHalfW - radiu / 2),  0);
        path.rCubicTo(-radiuControl, 0, -radiuControl, -textH - 2 * text2bg, 0, -textH - 2 * text2bg);
        path.rLineTo(textW - radiu, 0);
        path.rCubicTo(radiuControl, 0, radiuControl, textH + 2 * text2bg, 0, textH + 2 * text2bg);
        path.rLineTo(-(left + textW - selectX - triangleHalfW - radiu / 2), 0);
        path.close();
        prepareLinePaint(linePaint);
        linePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, linePaint);

        float textBaseLine = selectY - dip2Px(8) - triangleH - text2bg - fm.bottom ;
        canvas.drawText(dataText, left, textBaseLine, textPaint);
    }

    private float dip2Px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void setTextSize(Paint paint, int sp){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm);
        paint.setTextSize(px);
    }

    public void setValues(List<Value> values, int lineColor) {
        mValues = values;
        mLineColor = lineColor;
        float max = values.size() > 0 ? values.get(0).value : Float.MIN_VALUE;
        float min = values.size() > 0 ? values.get(0).value : Float.MAX_VALUE;
        for (int i = 1; i < values.size(); i++) {
            float value = values.get(i).value;
            if (max < value){
                max = value;
            }
            if (min > value){
                min = value;
            }
        }
        float top, bottom;
        if (max == Float.MIN_VALUE && min == Float.MAX_VALUE){
            top = 100;
            bottom = 0;
        } else if (max == min){
            top = max + 0.8f;
            bottom = min - 0.2f;
        } else if (min <= 0){
            top = max;
            bottom = min - (max - min) * 0.2f;
        } else {
            top = max;
            bottom = 0;
        }
        mChartFrame.setValueRange(top, bottom);
        requestLayout();
        selectData(-1);
    }

    public void setValueRange(float topValue, float bottomValue){
        mChartFrame.setValueRange(topValue, bottomValue);
        requestLayout();
    }

    public static class Value{
        public float value;
        public String key;

        public Value(String key, float value) {
            this.value = value;
            this.key = key;
        }
    }

    public interface DataTextFormat{
        String format(float value, int index);
    }

    private class ChartFrame {
        float chartLeft;
        float chartRight;
        float chartTop;
        float chartBottom;
        float chartHeight;
        float chartWidth;
        float topValue;
        float bottomValue;

        void setValueRange(float tValue, float bValue){
            bottomValue = bValue;
            topValue = tValue;
        }

        void setFrame(float left, float right, float top, float bottom){
            chartLeft = left;
            chartRight = right;
            chartTop = top;
            chartBottom = bottom;
            chartHeight = bottom - top;
            chartWidth = right - left;
        }

        float computeY(float value){
            if (mValues == null || mValues.size() == 0){
                return 0;
            }
            return chartTop + (topValue - value) * chartHeight / (topValue - bottomValue);
        }

        float computeX(float key){
            if (mValues == null || mValues.size() == 0){
                return -1;
            }
            if (mValues.size() == 1){
                return chartLeft + chartWidth / 2;
            }
            return chartLeft + key * chartWidth / (mValues.size() - 1);
        }

        int computeIndex(float x){
            if (x < 0 || x > getWidth()){
                return -1;
            }
            if (mValues == null){
                return -1;
            }
            int valueCount = mValues.size();
            if (valueCount < 1) {
                return -1;
            }
            if (valueCount == 1){
                return 0;
            }
            float dis = chartWidth / (valueCount - 1);
            float start = chartLeft - dis / 2;
            for (int index = 0; index < valueCount; index++) {
                start += dis;
                if (start > x){
                    return index;
                }
            }
            return -1;
        }
    }

    public interface OnDataSelectListener {
        void onDataSelect(int index, boolean isShow);
    }
}