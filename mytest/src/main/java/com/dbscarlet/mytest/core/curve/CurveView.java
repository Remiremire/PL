package com.dbscarlet.mytest.core.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by Daibing Wang on 2018/7/20.
 */
public class CurveView extends View {
    //曲线平滑度
    private static final float LINE_SMOOTHNESS = 0.16f;
    private List<CurveLine> mCurveLines;
    private Float mMinValueLimit;
    private Float mMaxValueLimit;
    private XAxes xAxes;
    private float defCurvePaddingLR = dip2Px(16);
    private float pointTouchRangePow = dip2Px(16) * dip2Px(16);
    private OnPointTouch onPointTouch;

    public CurveView(Context context) {
        super(context);
        initView();
    }

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        CurveLine.Point touchPoint = null;
        CurveLine touchLine = null;
        int touchIndex = 0;
        float touchDisPow = Float.MAX_VALUE;
        if (mCurveLines != null && mCurveLines.size() > 0) {
            for (CurveLine cl : mCurveLines) {
                List<CurveLine.Point> pointList = cl.getPointList();
                if (pointList == null || pointList.size() == 0) {
                    continue;
                }
                for (int i = 0; i < pointList.size(); i++) {
                    CurveLine.Point p = pointList.get(i);
                    float curDisPow = (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
                    if (curDisPow < pointTouchRangePow && (touchPoint == null || curDisPow < touchDisPow)) {
                        touchPoint = p;
                        touchDisPow = curDisPow;
                        touchLine = cl;
                        touchIndex = i;
                    }
                }
            }
        }
        if (onPointTouch != null && touchPoint != null) {
            onPointTouch.onPointTouch(touchLine, touchIndex, touchPoint);
        }
        return super.onTouchEvent(event);
    }

    public void setOnPointTouch(OnPointTouch onPointTouch) {
        this.onPointTouch = onPointTouch;
    }

    private void initView() {
    }

    public void setXAxes(XAxes xAxes) {
        this.xAxes = xAxes;
        notifyCurvesChange();
    }

    public void setBaseInfo(float minValueLimit, float maxValueLimit) {
        mMinValueLimit = minValueLimit;
        mMaxValueLimit = maxValueLimit;
    }

    public void setCurveLine(List<CurveLine> curveLine) {
        this.mCurveLines = curveLine;
        notifyCurvesChange();
    }

    public void notifyCurvesChange() {
        float top = getPaddingTop();
        float left = getPaddingLeft();
        float bottom = getHeight() - getPaddingBottom();
        float right = getWidth() - getPaddingRight();
        float curveT = top + dip2Px(24);
        float curveB = bottom;
        float curveL = left + defCurvePaddingLR;
        float curveR = right - defCurvePaddingLR;
        float xAxesStep = 0;
        if (xAxes != null) {
            curveB -= xAxes.getHeight();
            xAxes.compute(left, right, curveL, curveT, curveB, curveR);
            xAxesStep = xAxes.getXAexStep();
        }
        if (mCurveLines != null) {
            for (CurveLine cl : mCurveLines) {
                cl.compute(curveL, curveT, curveR, curveB,
                        mMinValueLimit, mMaxValueLimit, xAxesStep, LINE_SMOOTHNESS);
            }
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        notifyCurvesChange();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (xAxes != null) {
            xAxes.draw(canvas);
        }
        if (mCurveLines != null) {
            for (CurveLine cl : mCurveLines) {
                cl.drawLine(canvas);
            }
            for (CurveLine cl : mCurveLines) {
                cl.drawPoints(canvas);
            }
        }
    }

    private float dip2Px(float dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public interface OnPointTouch {
        void onPointTouch(CurveLine curveLine, int index, CurveLine.Point point);
    }
}