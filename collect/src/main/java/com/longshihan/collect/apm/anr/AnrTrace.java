package com.longshihan.collect.apm.anr;

import android.os.Handler;

import com.longshihan.collect.apm.anr.task.AnrHandleTask;
import com.longshihan.collect.apm.anr.task.LagHandleTask;
import com.longshihan.collect.apm.fps.UIThreadMonitor;
import com.longshihan.collect.apm.fps.listener.LooperObserver;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.plugin.IPlugin;
import com.longshihan.collect.utils.Constants;
import com.longshihan.collect.utils.MatrixHandlerThread;
import com.longshihan.collect.utils.MatrixLog;

public class AnrTrace extends LooperObserver implements IPlugin {

    private static final String TAG = "Matrix.AnrTracer";
    private Handler anrHandler;
    private Handler lagHandler;
    private volatile AnrHandleTask anrTask = new AnrHandleTask();
    private volatile LagHandleTask lagTask = new LagHandleTask();
    private boolean isAnrTraceEnable;
    private static AnrTrace instance;

    public static AnrTrace getInstance() {
        if (instance==null){
            instance=new AnrTrace();
        }
        return instance;
    }

    public AnrTrace() {

    }


    @Override
    public void init() {
        UIThreadMonitor.getMonitor().addObserver(this);
        this.anrHandler = new Handler(MatrixHandlerThread.getDefaultHandler().getLooper());
        this.lagHandler = new Handler(MatrixHandlerThread.getDefaultHandler().getLooper());
    }

    @Override
    public void start() {

    }

    @Override
    public void dispatchBegin(long beginNs, long cpuBeginNs, long token) {
        super.dispatchBegin(beginNs, cpuBeginNs, token);

        MatrixLog.v(TAG, "* [dispatchBegin] token:%s ", token);
        long cost = (System.nanoTime() - token) / Constants.TIME_MILLIS_TO_NANO;
        anrHandler.postDelayed(anrTask, Constants.DEFAULT_ANR - cost);
        lagHandler.postDelayed(lagTask, Constants.DEFAULT_NORMAL_LAG - cost);
    }

    @Override
    public void dispatchEnd(long beginNs, long cpuBeginMs, long endNs, long cpuEndMs, long token, boolean isVsyncFrame) {
        super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isVsyncFrame);

            long cost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
            MatrixLog.v(TAG, "[dispatchEnd] token:%s cost:%sms cpu:%sms usage:%s",
                    token, cost, cpuEndMs - cpuBeginMs, Utils.calculateCpuUsage(cpuEndMs - cpuBeginMs, cost));

        if (null != anrTask) {
            anrHandler.removeCallbacks(anrTask);
        }
        if (null != lagTask) {
            lagHandler.removeCallbacks(lagTask);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {
        UIThreadMonitor.getMonitor().removeObserver(this);
        anrHandler.removeCallbacksAndMessages(null);
        lagHandler.removeCallbacksAndMessages(null);
    }
}
