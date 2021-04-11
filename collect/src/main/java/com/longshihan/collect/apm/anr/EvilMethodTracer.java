package com.longshihan.collect.apm.anr;

import com.longshihan.collect.apm.anr.task.AnalyseTask;
import com.longshihan.collect.apm.fps.AppMethodBeat;
import com.longshihan.collect.apm.fps.UIThreadMonitor;
import com.longshihan.collect.apm.fps.listener.LooperObserver;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.plugin.IPlugin;
import com.longshihan.collect.utils.Constants;
import com.longshihan.collect.utils.MatrixHandlerThread;
import com.longshihan.collect.utils.MatrixLog;

import org.jetbrains.annotations.Nullable;

public class EvilMethodTracer extends LooperObserver implements IPlugin {
    private static final String TAG = "Matrix.EvilMethodTracer";
    private AppMethodBeat.IndexRecord indexRecord;
    private long[] queueTypeCosts = new long[3];
    private long evilThresholdMs=2000;
    private boolean isEvilMethodTraceEnable;
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
        indexRecord = AppMethodBeat.getInstance().maskIndex("EvilMethodTracer#dispatchBegin");
    }

    @Override
    public void dispatchEnd(long beginNs, long cpuBeginMs, long endNs, long cpuEndMs, long token, boolean isVsyncFrame) {
        super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isVsyncFrame);
        long start = System.currentTimeMillis();
        long dispatchCost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
        try {
            if (dispatchCost >= evilThresholdMs) {
                long[] data = AppMethodBeat.getInstance().copyData(indexRecord);
                long[] queueCosts = new long[3];
                System.arraycopy(queueTypeCosts, 0, queueCosts, 0, 3);
                String scene = AppMethodBeat.getVisibleScene();
                MatrixHandlerThread.getDefaultHandler().post(new AnalyseTask(true, scene, data, queueCosts, cpuEndMs - cpuBeginMs, dispatchCost, endNs / Constants.TIME_MILLIS_TO_NANO));
            }
        } finally {
            indexRecord.release();

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
