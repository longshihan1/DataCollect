package com.longshihan.collect.apm.anr.task;

import android.util.Log;

public class AnalyseTask implements Runnable {


    public AnalyseTask() {

    }

    void analyse() {
        Log.d("测试", "AnalyseTask");


    }

    @Override
    public void run() {
        analyse();
    }
}
