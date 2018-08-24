package com.dbscarlet.applib.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private float defCurvePaddingLR = dip2Px(12);
    private float pointTouchRangePow = dip2Px(20) * dip2Px(20);
    private OnSelectPointListener onSelectPointListener;
    private RectF curveRectF = new RectF();

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

    private void initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && onSelectPointListener != null) {
            float x = event.getX();
            float y = event.getY();
            CurveLine.Point touchPoint = null;
            CurveLine touchLine = null;
            int lineIndex = 0;
            int pointIndex = 0;
            float touchDisPow = Float.MAX_VALUE;
            if (mCurveLines != null && mCurveLines.size() > 0) {
                for (int j = 0; j < mCurveLines.size(); j++) {
                    CurveLine cl = mCurveLines.get(j);
                    List<CurveLine.Point> pointList = cl.getPointList();
                    if (!cl.isVisible() || !cl.isCanSelect() || pointList == null || pointList.size() == 0) {
                        continue;
                    }
                    for (int i = 0; i < pointList.size(); i++) {
                        CurveLine.Point p = pointList.get(i);
                        float curDisPow = (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
                        if (curDisPow < pointTouchRangePow && (touchPoint == null || curDisPow < touchDisPow)) {
                            touchPoint = p;
                            touchDisPow = curDisPow;
                            touchLine = cl;
                            pointIndex = i;
                            lineIndex = j;
                        }
                    }
                }
            }
            if (touchPoint != null) {
                onSelectPointListener.onPointSelect(lineIndex, touchLine, pointIndex, touchPoint, this);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnSelectPointListener(OnSelectPointListener onSelectPointListener) {
        this.onSelectPointListener = onSelectPointListener;
    }

    public void setXAxes(XAxes xAxes) {
        this.xAxes = xAxes;
        notifyChange();
    }

    /**
     * 设置曲线图取值范围限制
     * @param minValueLimit 最小值，即y轴原点的值
     * @param maxValueLimit 最大值，即y轴在图顶部的值
     */
    public void setValueLimit(float minValueLimit, float maxValueLimit) {
        mMinValueLimit = minValueLimit;
        mMaxValueLimit = maxValueLimit;
    }

    public void setCurveLine(List<CurveLine> curveLine) {
        this.mCurveLines = curveLine;
        notifyChange();
    }

    public void notifyChange() {
        post(new Runnable() {
            @Override
            public void run() {
                float top = getPaddingTop();
                float left = getPaddingLeft();
                float bottom = getHeight() - getPaddingBottom();
                float right = getWidth() - getPaddingRight();
                curveRectF.top = top + dip2Px(24);
                curveRectF.bottom = bottom;
                curveRectF.left = left + defCurvePaddingLR;
                curveRectF.right = right - defCurvePaddingLR;
                float xAxesStep = 0;
                if (xAxes != null) {
                    curveRectF.bottom -= xAxes.getHeight();
                    xAxes.compute(left, right, curveRectF);
                    xAxesStep = xAxes.getXAexStep();
                }
                if (mCurveLines != null) {
                    for (CurveLine cl : mCurveLines) {
                        cl.compute(curveRectF, mMinValueLimit, mMaxValueLimit, xAxesStep, LINE_SMOOTHNESS);
                    }
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        notifyChange();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (xAxes != null) {
            xAxes.draw(canvas);
        }
        if (mCurveLines != null && mCurveLines.size() > 0) {
            canvas.save();
            canvas.clipRect(curveRectF);
            for (CurveLine cl : mCurveLines) {
                if (cl.isVisible()) {
                    cl.drawLine(canvas);
                }
            }
            for (CurveLine cl : mCurveLines) {
                if (cl.isVisible()) {
                    cl.drawPoints(canvas);
                }
            }
            canvas.restore();
        }
    }

    private float dip2Px(float dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public interface OnSelectPointListener {
        void onPointSelect(int lineIndex, CurveLine curveLine, int pointIndex, CurveLine.Point point, CurveView curveView);
    }

    public static class ShowLastSelectPointListener implements OnSelectPointListener {
        private Map<CurveView, CurveLine.Point> selectPoints = new HashMap<>();

        @Override
        public void onPointSelect(int lineIndex, CurveLine curveLine, int pointIndex, CurveLine.Point point, CurveView curveView) {
            CurveLine.Point lastShowPoint = selectPoints.get(curveView);
            if (lastShowPoint == null) {
                point.show = true;
                selectPoints.put(curveView, point);
            } else if (lastShowPoint == point) {
                point.show = false;
                selectPoints.put(curveView, null);
            } else {
                lastShowPoint.show = false;
                point.show = true;
                selectPoints.put(curveView, point);
            }
            curveView.notifyChange();
        }
    }
}