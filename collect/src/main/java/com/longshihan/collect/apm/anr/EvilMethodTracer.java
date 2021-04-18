package com.longshihan.collect.apm.anr;

import android.util.Log;

import com.longshihan.collect.apm.anr.task.AnalyseTask;
import com.longshihan.collect.apm.fps.UIThreadMonitor;
import com.longshihan.collect.apm.fps.listener.LooperObserver;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.plugin.IPlugin;
import com.longshihan.collect.utils.Constants;
import com.longshihan.collect.utils.MatrixHandlerThread;
import com.longshihan.collect.utils.MatrixLog;

import org.jetbrains.annotations.Nullable;

/**
 * 慢方法追踪
 */
public class EvilMethodTracer extends LooperObserver implements IPlugin {
    private static final String TAG = "Matrix.EvilMethodTracer";
    private long[] queueTypeCosts = new long[3];
    private long evilThresholdMs=700;
    private static EvilMethodTracer instance;

    public static EvilMethodTracer getInstance() {
        if (instance==null){
            instance=new EvilMethodTracer();
        }
        return instance;
    }

    @Override
    public void init() {
        UIThreadMonitor.getMonitor().addObserver(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void dispatchBegin(long beginNs, long cpuBeginNs, long token) {
        super.dispatchBegin(beginNs, cpuBeginNs, token);
    }

    @Override
    public void dispatchEnd(long beginNs, long cpuBeginMs, long endNs, long cpuEndMs, long token, boolean isVsyncFrame) {
        super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isVsyncFrame);
        long start = System.currentTimeMillis();
        long dispatchCost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
        try {
            if (dispatchCost >= evilThresholdMs) {
                Log.d("测试","evilThresholdMs");
                //直接打标记
                MatrixHandlerThread.getDefaultHandler().post(new AnalyseTask());
            }
        } finally {
                String usage = Utils.calculateCpuUsage(cpuEndMs - cpuBeginMs, dispatchCost);
                MatrixLog.v(TAG, "[dispatchEnd] token:%s cost:%sms cpu:%sms usage:%s innerCost:%s",
                        token, dispatchCost, cpuEndMs - cpuBeginMs, usage, System.currentTimeMillis() - start);

        }
    }

    @Override
    public void doFrame(@Nullable String focusedActivity, long startNs, long endNs, boolean isVsyncFrame, long intendedFrameTimeNs, long inputCostNs, long animationCostNs, long traversalCostNs) {
        super.doFrame(focusedActivity, startNs, endNs, isVsyncFrame, intendedFrameTimeNs, inputCostNs, animationCostNs, traversalCostNs);
        queueTypeCosts[0] = inputCostNs;
        queueTypeCosts[1] = animationCostNs;
        queueTypeCosts[2] = traversalCostNs;
    }

    @Override
    public void stop() {

    }

    public void modifyEvilThresholdMs(long evilThresholdMs) {
        this.evilThresholdMs = evilThresholdMs;
    }

    @Override
    public void destory() {
        UIThreadMonitor.getMonitor().removeObserver(this);
    }
}