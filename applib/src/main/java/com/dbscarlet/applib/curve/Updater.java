package com.dbscarlet.applib.curve;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Daibing Wang on 2018/8/22.
 */
public abstract class Updater<T, R> {
    private int lineWidth;
    private long duration = 1000;
    private long lastUpdateTime = 0;
    private float offset = -1;
    private final List<R> dataList = new LinkedList<>();
    private final Queue<T> dataCache = new PriorityBlockingQueue<>();

    public Updater(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void tryUpdate() {
        long time = System.currentTimeMillis();
        long timeDiff = time - lastUpdateTime;
        if (timeDiff >= duration) {
            if (dataList.size() > lineWidth + 2) {
                dataList.remove(0);
                lastUpdateTime = time;
                offset = -1;
            }
            tryPollData();
        } else {
            offset = -1 - timeDiff * 1.0f / duration;
        }
        onUpdate(dataList, offset);
    }

    private void tryPollData() {
        while (dataList.size() < lineWidth + 3 && !dataCache.isEmpty()) {
            T next = dataCache.poll();
            if (next != null) {
                dataList.add(convert(next));
            }
        }
    }

    protected abstract R convert(@NotNull T data);

    protected abstract void onUpdate(List<R> dataList, float offset);


    public void addData(T newData) {
        if (newData == null) return;

        if (dataList.size() < lineWidth + 3) {
            R data = convert(newData);
            if (dataList.size() == 0) {
                dataList.add(convert(newData));
                dataList.add(convert(newData));
                offset = -2;
            }
            dataList.add(data);
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
