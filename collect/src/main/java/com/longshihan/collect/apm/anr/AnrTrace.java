package com.longshihan.collect.apm.anr;

import android.os.Handler;
import android.util.Log;

import com.longshihan.collect.apm.anr.task.AnalyseTask;
import com.longshihan.collect.apm.anr.task.AnrHandleTask;
import com.longshihan.collect.apm.anr.task.LagHandleTask;
import com.longshihan.collect.apm.fps.UIThreadMonitor;
import com.longshihan.collect.apm.fps.listener.LooperObserver;
import com.longshihan.collect.init.TraceManager;
import com.longshihan.collect.plugin.IPlugin;
import com.longshihan.collect.traceTime.TraceTime;
import com.longshihan.collect.utils.Constants;
import com.longshihan.collect.utils.MatrixHandlerThread;
import com.longshihan.collect.utils.data.EvilMMAPUtils;

import org.jetbrains.annotations.Nullable;

import static com.longshihan.collect.utils.FileUtils.CheckOtherDate;

public class AnrTrace extends LooperObserver implements IPlugin {

    private static final String TAG = "Matrix.AnrTracer";
    private Handler anrHandler;
    private Handler lagHandler;
    private volatile AnrHandleTask anrTask = new AnrHandleTask();
    private volatile LagHandleTask lagTask = new LagHandleTask();
    private static AnrTrace instance;
    private long[] queueTypeCosts = new long[3];
    public static long evilThresholdMs = 1000;

    public static AnrTrace getInstance() {
        if (instance == null) {
            instance = new AnrTrace();
        }
        return instance;
    }

    public AnrTrace() {

    }


    @Override
    public void init() {
        EvilMMAPUtils.defaultEvilinit(TraceManager.mContext, CheckOtherDate());
        UIThreadMonitor.getMonitor().addObserver(this);
        Constants.DEFAULT_ANR= TraceManager.settingInfo.getAnrTime();
        evilThresholdMs = TraceManager.settingInfo.getEvilTime();
        this.anrHandler = new Handler(MatrixHandlerThread.getDefaultHandler().getLooper());
        this.lagHandler = new Handler(MatrixHandlerThread.getDefaultHandler().getLooper());
    }

    @Override
    public void start() {

    }

    @Override
    public void dispatchBegin(long beginNs, long cpuBeginNs, long token) {
        super.dispatchBegin(beginNs, cpuBeginNs, token);
        TraceTime.onFrameStart();
//        MatrixLog.v(TAG, "* [dispatchBegin] token:%s ", token);
        long cost = (System.nanoTime() - token) / Constants.TIME_MILLIS_TO_NANO;
        anrHandler.postDelayed(anrTask, Constants.DEFAULT_ANR - cost);
        lagHandler.postDelayed(lagTask, Constants.DEFAULT_NORMAL_LAG - cost);
    }

    @Override
    public void doFrame(@Nullable String focusedActivity, long startNs, long endNs, boolean isVsyncFrame, long intendedFrameTimeNs, long inputCostNs, long animationCostNs, long traversalCostNs) {
        super.doFrame(focusedActivity, startNs, endNs, isVsyncFrame, intendedFrameTimeNs, inputCostNs, animationCostNs, traversalCostNs);
        queueTypeCosts[0] = inputCostNs;
        queueTypeCosts[1] = animationCostNs;
        queueTypeCosts[2] = traversalCostNs;
    }

    @Override
    public void dispatchEnd(long beginNs, long cpuBeginMs, long endNs, long cpuEndMs, long token, boolean isVsyncFrame) {
        super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isVsyncFrame);
        long start = System.currentTimeMillis();
        long dispatchCost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
        try {
            if (dispatchCost >= evilThresholdMs) {

                Log.d("测试", "evilThresholdMs" + queueTypeCosts[0] + ":" + queueTypeCosts[1] + ":" + queueTypeCosts[2]);
                Log.d("测试", "EvilMethodTracer:" + TraceTime.onFrameStack());
                EvilMMAPUtils.saveEvilValue(queueTypeCosts[0] + ":" + queueTypeCosts[1] + ":" + queueTypeCosts[2]);
                EvilMMAPUtils.saveEvilValue(TraceTime.onFrameStack().toString());
                //直接打标记
                MatrixHandlerThread.getDefaultHandler().post(new AnalyseTask());
            }
        } finally {
            if (null != anrTask) {
                anrHandler.removeCallbacks(anrTask);
            }
            if (null != lagTask) {
                lagHandler.removeCallbacks(lagTask);
            }
//                String usage = Utils.calculateCpuUsage(cpuEndMs - cpuBeginMs, dispatchCost);
//                MatrixLog.v(TAG, "[dispatchEnd] token:%s cost:%sms cpu:%sms usage:%s innerCost:%s",
//                        token, dispatchCost, cpuEndMs - cpuBeginMs, usage, System.currentTimeMillis() - start);

        }
//            long cost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
//            MatrixLog.v(TAG, "[dispatchEnd] token:%s cost:%sms cpu:%sms usage:%s",
//                    token, cost, cpuEndMs - cpuBeginMs, Utils.calculateCpuUsage(cpuEndMs - cpuBeginMs, cost));


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

    @Override
    public boolean getState() {
        return false;
    }
}
