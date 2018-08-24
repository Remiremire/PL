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
    private final int TIME_IN_FRAME = 30;
    private boolean isUpdate = false;
    private Handler handler;
    private List<Updater> updaterList = new LinkedList<>();

    public UpdateThread() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                for (Updater updater : updaterList) {
                    updater.tryUpdate();
                }
            }
        };
    }

    public void addUpdater(Updater updater) {
        updaterList.add(updater);
    }

    @Override
    public void run() {
        while (isUpdate) {
            waitNextUpdateTime();
            handler.sendEmptyMessage(1);
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
