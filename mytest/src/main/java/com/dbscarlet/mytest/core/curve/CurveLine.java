package com.dbscarlet.mytest.core.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daibing Wang on 2018/7/20.
 */
public class CurveLine {
    private List<Point> mPointList = new ArrayList<>();
    private Computer mComputer;
    private int lineColor;
    private GradientDrawable lineGradient;
    private Path mCurveLinePath = new Path();
    private Path mCurveLineRangePath = new Path();
    private Path mTextPath = new Path();
    private Paint textPaint, pathPaint;
    private boolean canSelect = true;
    public CurveLine(Context context, int lineColor) {
        int defGradientBottom = 0x00000000;
        int defGradientTop = Color.argb((int) (Color.alpha(lineColor) * 0.3),
                Color.red(lineColor), Color.green(lineColor), Color.blue(lineColor));
        setColor(lineColor, defGradientTop, defGradientBottom);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mComputer = new Computer(displayMetrics);
        textPaint = new Paint();
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics));
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        pathPaint = new Paint();
        pathPaint.setStrokeWidth(mComputer.dip2Px(2));
        pathPaint.setColor(lineColor);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setAntiAlias(true);
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public void setPointList(List<Point> pointList) {
        mPointList = pointList;
    }

    public List<Point> getPointList() {
        return mPointList;
    }

    public void setColor(int lineColor, int gradientTop, int gradientBottom) {
        this.lineColor = lineColor;
        lineGradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{gradientTop, gradientBottom});
        lineGradient.setShape(GradientDrawable.RECTANGLE);
        lineGradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    public void compute(float left, float top, float right, float bottom,
                        float minValueLimit, float maxValueLimit, float xAxesStep, float LINE_SMOOTHNESS) {
        computeLine(left, top, right, bottom, minValueLimit, maxValueLimit, xAxesStep, LINE_SMOOTHNESS);
    }

    private void computeLine(float left, float top, float right, float bottom,
                             float minValue, float maxValue,float xAxesStep, float LINE_SMOOTHNESS) {
        mCurveLineRangePath.reset();
        final Path path = mCurveLinePath;
        path.reset();
        final List<Point> pointList = mPointList;
        if (pointList == null || pointList.size() == 0) {
            return;
        }
        mComputer.set(top, left, bottom, right, maxValue, minValue, xAxesStep);
        final int pointSize = pointList.size();
        for (int i = 0; i < pointSize; i++) {
            mComputer.computePoint(pointList.get(i), i);
        }
        if (pointSize < 1) {
            return;
        }
        Point prePreviousPoint = pointList.get(0);
        Point previousPoint = pointList.get(0);
        Point currentPoint = pointList.get(0);
        Point nextPoint = pointList.get(0);
        for (int pointIndex = 0; pointIndex < pointSize; ++pointIndex) {
            prePreviousPoint = previousPoint;
            previousPoint = currentPoint;
            currentPoint = nextPoint;
            // nextPoint is always new one or it is equal currentPoint.
            if (pointIndex < pointSize - 1) {
                nextPoint = pointList.get(pointIndex + 1);
            }
            if (pointIndex == 0) {
                // Move to start point.
                path.moveTo(currentPoint.x, currentPoint.y);
            } else {
                // Calculate control points.
                final float firstDiffX = (currentPoint.x - prePreviousPoint.x);
                final float firstDiffY = (currentPoint.y - prePreviousPoint.y);
                final float secondDiffX = (nextPoint.x - previousPoint.x);
                final float secondDiffY = (nextPoint.y - previousPoint.y);
                float firstControlPointX = previousPoint.x + (LINE_SMOOTHNESS * firstDiffX);
                float firstControlPointY = previousPoint.y + (LINE_SMOOTHNESS * firstDiffY);
                float secondControlPointX = currentPoint.x - (LINE_SMOOTHNESS * secondDiffX);
                float secondControlPointY = currentPoint.y - (LINE_SMOOTHNESS * secondDiffY);

                /////////////////连续三个值相同认为是直线，重新修正控制点//////////////
                if (currentPoint.y == previousPoint.y && currentPoint.y == prePreviousPoint.y && pointIndex > 2) {
                    secondControlPointY = currentPoint.y;
                }
                if (pointIndex > 3 && prePreviousPoint.y == previousPoint.y) {
                    float prePrePreviousPointY = pointList.get(pointIndex - 3).y;
                    if (prePreviousPoint.y == prePrePreviousPointY) {
                        firstControlPointX = previousPoint.x + (currentPoint.x - previousPoint.x) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        firstControlPointY = previousPoint.y;
                    }
                }
                if (currentPoint.y == previousPoint.y && currentPoint.y == nextPoint.y && pointIndex + 1 < pointSize) {
                    firstControlPointY = currentPoint.y;
                }
                if (pointIndex + 2 < pointSize && currentPoint.y == nextPoint.y) {
                    float nextNextPointY = pointList.get(pointIndex + 2).y;
                    if (nextNextPointY == nextPoint.y) {
                        secondControlPointX = currentPoint.x - (currentPoint.x - previousPoint.x) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        secondControlPointY = currentPoint.y;
                    }
                }

                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPoint.x, currentPoint.y);
            }
        }

        float lineStartX, lineEndX;
        if (pointSize == 1) {
//            path.rMoveTo(-dip2Px(5), 0);
            path.rLineTo(mComputer.dip2Px(10), 0);
            lineStartX = pointList.get(0).x - mComputer.dip2Px(5);
            lineEndX = pointList.get(0).x + mComputer.dip2Px(10);
        } else {
            lineStartX = pointList.get(0).x;
            lineEndX = pointList.get(pointSize - 1).x;
        }

        mCurveLineRangePath.set(path);
        mCurveLineRangePath.lineTo(lineEndX, bottom);
        mCurveLineRangePath.lineTo(lineStartX, bottom);
        mCurveLineRangePath.close();
        lineGradient.setBounds((int) lineStartX, 0, (int)lineEndX, (int)bottom);
    }

    public void drawLine(Canvas canvas) {
        if (mPointList == null || mPointList.size() == 0) {
            return;
        }
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setColor(lineColor);
        canvas.drawPath(mCurveLinePath, pathPaint);
        canvas.save();
        canvas.clipPath(mCurveLineRangePath);
        lineGradient.draw(canvas);
        canvas.restore();
    }

    public void drawPoints(Canvas canvas) {
        final List<Point> pointList = mPointList;
        for (Point point : pointList) {
            if (point.show) {
                drawPoint(point, canvas);
            }
        }
    }

    private void drawPoint(Point point, Canvas canvas) {
        pathPaint.setStyle(Paint.Style.FILL);
        mTextPath.reset();
        mTextPath.addCircle(point.x, point.y, mComputer.dip2Px(3.5f), Path.Direction.CW);
        mTextPath.close();
        canvas.drawPath(mTextPath, pathPaint);
        if (!TextUtils.isEmpty(point.text)) {
            float controlXOffset = mComputer.textPaddingLr * 4f;
            float controlYOffset = controlXOffset / 3;
            mTextPath.reset();
            mTextPath.moveTo(point.textLeft, point.textTop - mComputer.textPaddingTb);
            mTextPath.rLineTo(point.textWidth, 0);
            mTextPath.rCubicTo(controlXOffset, controlYOffset,
                    controlXOffset, point.textHeight + mComputer.textPaddingTb * 2 - controlYOffset,
                    0, point.textHeight + mComputer.textPaddingTb * 2);
            mTextPath.rLineTo(-point.textWidth, 0);
            mTextPath.rCubicTo(-controlXOffset, -controlYOffset,
                    -controlXOffset, -point.textHeight - mComputer.textPaddingTb * 2 + controlYOffset,
                    0, -point.textHeight - mComputer.textPaddingTb * 2);
            mTextPath.close();
            canvas.drawPath(mTextPath, pathPaint);
            canvas.drawText(point.text, point.textLeft, point.textBaseLine, textPaint);
        }
    }

    private class Computer {
        private float textPaddingLr;
        private float textPaddingTb;
        private final DisplayMetrics displayMetrics;
        float top;
        float left;
        float bottom;
        float right;
        float maxValue;
        float minValue;
        float xAxesStep;
        int startIndex = 0;

        public Computer(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }

        private void set(float top, float left, float bottom, float right, float maxValue, float minValue, float xAxesStep) {
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.xAxesStep = xAxesStep;
            textPaddingLr = dip2Px(3);
            textPaddingTb = dip2Px(1);
        }

        private void computePoint(Point point, int index) {
            point.x = left + xAxesStep * (index + startIndex);
            point.y = bottom + (top - bottom) * (point.value / (maxValue - minValue));
            if (TextUtils.isEmpty(point.text)) return;
            point.textWidth = textPaint.measureText(point.text);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            point.textBaseLine = point.y - dip2Px(6) - fm.bottom - textPaddingTb;
            point.textTop = point.textBaseLine + fm.top;
            point.textHeight = fm.bottom - fm.top;
            if (point.x - point.textWidth / 2 - textPaddingLr * 3 < left) {
                point.textLeft = left + textPaddingLr * 3;
            } else if (point.x + point.textWidth / 2 + textPaddingLr * 3 > right) {
                point.textLeft = right - point.textWidth - textPaddingLr * 3;
            } else {
                point.textLeft = point.x - point.textWidth / 2;
            }
        }

        private float dip2Px(float dip) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
        }
    }
    
    public static class Point{
        public float value;
        public boolean show;
        public String text;
        float x;
        float y;
        private float textTop;
        private float textLeft;
        private float textBaseLine;
        private float textWidth;
        private float textHeight;

        public Point(float value) {
            this(value, false, value + "");
        }
        public Point(float value, boolean show) {
            this(value, show, value + "");
        }
        public Point(float value, String text) {
            this(value, false, text);
        }
        public Point(float value, boolean show, String text) {
            this.value = value;
            this.show = show;
            this.text = text;
        }
    }


}
