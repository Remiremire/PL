package com.dbscarlet.applib.curve;

import android.os.Message;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Daibing Wang on 2018/8/22.
 */
public abstract class CurveLineUpdater<T> {
    private CurveLine curveLine;
    private int curveLineWidth;
    private long duration = 1000;
    private long lastUpdateTime;
    private float lineOffset = -1;
    private final List<CurveLine.Point> pointList = new LinkedList<>();
    private final Queue<T> dataCache = new PriorityBlockingQueue<>();

    public void onUpdate(Message msg) {
        long time = System.currentTimeMillis();
        switch (msg.what) {
            case UpdateThread.INIT:
                pointList.add(new CurveLine.Point(pointList.get(0).value));
                lastUpdateTime = time;
                lineOffset = -1;
                break;
            case UpdateThread.UPDATE:
                long timeDiff = time - lastUpdateTime;
                if (timeDiff >= duration) {
                    if (pointList.size() > curveLineWidth + 2) {
                        pointList.remove(0);
                        lastUpdateTime = time;
                        lineOffset = -1;
                    }
                    tryPollData();
                } else {
                    lineOffset = -1 - timeDiff * 1.0f / duration;
                }
                break;
        }
        if (curveLine != null) {
            curveLine.setPointList(pointList);
            curveLine.setLineLeftOffset(lineOffset);
        }
    }

    private void tryPollData() {
        while (pointList.size() < curveLineWidth + 3 && !dataCache.isEmpty()) {
            T next = dataCache.poll();
            if (next != null) {
                pointList.add(convert(next));
            }
        }
    }

    public void setCurveInfo(CurveLine curveLine, int curveLineWidth) {
        this.curveLine = curveLine;
        this.curveLineWidth = curveLineWidth;
    }

    protected abstract CurveLine.Point convert(@NotNull T data);


    public void addData(T newData) {
        if (newData == null) return;

        if (pointList.size() < curveLineWidth + 3) {
            CurveLine.Point point = convert(newData);
            if (pointList.size() == 0) {
                pointList.add(new CurveLine.Point(point.value));
            }
            pointList.add(point);
        } else {
            dataCache.add(newData);
        }
    }

    public void addDataList(List<T> dataList) {
        for (T d : dataList) {
            addData(d);
        }
    }

    public int getCacheSize() {
        return dataCache.size();
    }
}
