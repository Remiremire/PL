package com.dbscarlet.applib.curve;

import android.os.Message;

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
    private long duration = 1000;
    private long lastUpdateTime;
    private final Queue<T> dataResource = new PriorityBlockingQueue<>();

    public void onUpdate(Message msg) {
        List<CurveLine.Point> pointList = curveLine.getPointList();
        long time = System.currentTimeMillis();
        switch (msg.what) {
            case UpdateThread.INIT:
                pointList.add(new CurveLine.Point(pointList.get(0).value));
                curveLine.setLineLeftOffset(-1);
                lastUpdateTime = time;
                break;
            case UpdateThread.UPDATE:
                long timeDiff = time - lastUpdateTime;
                if (timeDiff >= duration) {
                    if (pointList.size() > curveLineWidth + 2) {
                        pointList.remove(0);
                        lastUpdateTime = time;
                        curveLine.setLineLeftOffset(-1);
                    }
                    if (pointList.size() < curveLineWidth + 3 && !dataResource.isEmpty()) {
                        T next = dataResource.poll();
                        pointList.add(convert(next));
                        if (pointList.size() < curveLineWidth + 4 && !dataResource.isEmpty()) {
                            T nextNext = dataResource.poll();
                            if (nextNext != null) {
                                pointList.add(convert(nextNext));
                            }
                        }
                    }
                } else {
                    curveLine.setLineLeftOffset(-1 - timeDiff * 1.0f / duration);
                }
                break;
        }
    }

    public void setCurveInfo(CurveView curveView, CurveLine curveLine, int curveLineWidth) {
        this.curveView = curveView;
        this.curveLine = curveLine;
        this.curveLineWidth = curveLineWidth;
    }

    protected abstract CurveLine.Point convert(T data);


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
        }
    }

    public int getCashSize() {
        return dataResource.size();
    }
}
