package com.longshihan.collect.control;

import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.fps.TraceFPSDataInfo;
import com.longshihan.collect.model.fps.TraceFPSInfo;
import com.longshihan.collect.model.lifecycle.TraceLifecycleInfo;
import com.longshihan.collect.traceTime.TraceTime;
import com.longshihan.collect.ui.time.CurrentTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * todo 保存会生成对象，性能影响比较大，改成long类型数据
 */
public class TraceControl {
    public static List<TraceFPSInfo> traceFPSInfos = new ArrayList<>();
    public static List<TraceFPSDataInfo> traceFPSDataInfos = new ArrayList<>();
    public static List<TraceLifecycleInfo> traceLifecycleInfos = new ArrayList<>();

    public static void saveActivityLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, true, state,
                Utils.sdf.format(CurrentTimeUtils.currentTime)));
    }

    public static void saveFragmentLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, false, state,
                Utils.sdf.format(CurrentTimeUtils.currentTime)));
    }

    public static void saveFps(int fps) {
        traceFPSInfos.add(new TraceFPSInfo(fps,
                Utils.sdf.format(System.currentTimeMillis())));
    }

    public static void saveFpsData(long inputCostNs, long animationCostNs, long traversalCostNs) {
        traceFPSDataInfos.add(new TraceFPSDataInfo(inputCostNs, animationCostNs, traversalCostNs,
                Utils.sdfTime.format(System.currentTimeMillis())));
    }

    public static void clearList() {
        traceFPSInfos.clear();
        traceLifecycleInfos.clear();
        TraceTime.clear();
    }
}
