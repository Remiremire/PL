package com.dbscarlet.applib.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Daibing Wang on 2018/8/22.
 */
public abstract class CurveLineUpdater<T> {
    private CurveView curveView;
    private CurveLine curveLine;
    private int curveLineWidth;
    private final Queue<T> dataResource = new PriorityBlockingQueue<>();
    private long updateDuration = 1000;
    private ValueAnimator animator = new ValueAnimator();
    private boolean isUpdate;

    public CurveLineUpdater() {
        animator.setFloatValues(-1f, -2f);
        animator.setDuration(updateDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (float) animation.getAnimatedValue();
                curveLine.setLineLeftOffset(offset);
                curveView.notifyChange();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationRepeat(Animator animation) {
                List<CurveLine.Point> pointList = curveLine.getPointList();
                if (pointList.size() > curveLineWidth + 1) {
                    pointList.remove(0);
                }
                curveLine.setLineLeftOffset(-1);

                if (pointList.size() == curveLineWidth + 1 && dataResource.size() == 0) {
                    animation.pause();
                    curveView.notifyChange();
                } else {
                    if (pointList.size() < curveLineWidth + 3) {
                        T next = dataResource.poll();
                        if (next != null) {
                            pointList.add(convert(next));
                        }
                        if (pointList.size() < curveLineWidth + 4) {
                            T nextNext = dataResource.poll();
                            if (nextNext != null) {
                                pointList.add(convert(nextNext));
                            }
                        }
                    }
                    curveView.notifyChange();
                }
            }

        });
    }

    public void setCurveInfo(CurveView curveView, CurveLine curveLine, int curveLineWidth) {
        this.curveView = curveView;
        this.curveLine = curveLine;
        this.curveLineWidth = curveLineWidth;
    }

    protected abstract CurveLine.Point convert(T data);

    public void stopUpdate() {
        animator.end();
        isUpdate = false;
    }

    private void startUpdate() {
        if (isUpdate) return;

        List<CurveLine.Point> pointList = curveLine.getPointList();
        pointList.add(new CurveLine.Point(pointList.get(0).value));
        curveLine.setLineLeftOffset(-1);
        curveView.notifyChange();
        isUpdate = true;
        animator.start();
    }

    public void addData(T newData) {
        List<CurveLine.Point> pointList = curveLine.getPointList();
        if (pointList.size() < curveLineWidth) {
            pointList.add(convert(newData));
            curveView.notifyChange();
        } else {
            if (pointList.size() < curveLineWidth + 3) {
                pointList.add(convert(newData));
            } else {
                dataResource.add(newData);
            }
            if (!isUpdate) {
                startUpdate();
            }
            animator.resume();
        }
    }

    public int getCashSize() {
        return dataResource.size();
    }
}
