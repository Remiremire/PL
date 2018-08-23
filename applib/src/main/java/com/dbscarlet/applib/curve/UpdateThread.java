package com.dbscarlet.applib.curve;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daibing Wang on 2018/8/23.
 */
public class UpdateThread extends Thread{
    public static final int INIT = 0;
    public static final int UPDATE = 1;
    private final int TIME_IN_FRAME = 30;
    private boolean isUpdate = false;
    private Handler handler;
    private List<CurveLineUpdater> updaterList = new LinkedList<>();
    private CurveView curveView;

    public UpdateThread() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                for (CurveLineUpdater updater : updaterList) {
                    updater.onUpdate(msg);
                }
                curveView.notifyChange();
            }
        };
    }

    public void setCurveView(CurveView curveView) {
        this.curveView = curveView;
    }

    public void addUpdater(CurveLineUpdater updater) {
        updaterList.add(updater);
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(INIT);
        while (isUpdate) {
            waitNextUpdateTime();
            handler.sendEmptyMessage(UPDATE);
        }
    }

    /**
     * 限制刷新帧数
     */
    private void waitNextUpdateTime() {
        long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) <= TIME_IN_FRAME) {
            try {
                Thread.sleep(TIME_IN_FRAME / 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopUpdate() {
        isUpdate = false;
    }

    public void startUpdate() {
        isUpdate = true;
        start();
    }
}
