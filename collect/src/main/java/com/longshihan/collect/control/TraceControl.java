package com.longshihan.collect.control;

import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.fps.TraceFPSInfo;
import com.longshihan.collect.model.lifecycle.TraceLifecycleInfo;
import com.longshihan.collect.traceTime.TraceTime;

import java.util.ArrayList;
import java.util.List;

public class TraceControl {
    public static List<TraceFPSInfo> traceFPSInfos = new ArrayList<>();
    public static List<TraceLifecycleInfo> traceLifecycleInfos = new ArrayList<>();

    public static void saveActivityLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, true, state,
                Utils.sdf.format(System.currentTimeMillis())));
    }

    public static void saveFragmentLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, false, state,
                Utils.sdf.format(System.currentTimeMillis())));
    }

    public static void saveFps(int fps) {
        traceFPSInfos.add(new TraceFPSInfo(fps,
                Utils.sdf.format(System.currentTimeMillis())));
    }

    public static void clearList() {
        traceFPSInfos.clear();
        traceLifecycleInfos.clear();
        TraceTime.clear();
    }
}
