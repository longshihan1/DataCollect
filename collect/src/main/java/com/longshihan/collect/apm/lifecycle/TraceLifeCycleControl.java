package com.longshihan.collect.apm.lifecycle;

import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.lifecycle.TraceLifecycleInfo;

import java.util.ArrayList;
import java.util.List;

public class TraceLifeCycleControl {
    public static List<TraceLifecycleInfo> traceLifecycleInfos = new ArrayList<>();

    public static void saveActivityLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, true, state,
                Utils.sdf.format(System.currentTimeMillis())));
    }

    public static void saveFragmentLifeCycleState(String className, String state) {
        traceLifecycleInfos.add(new TraceLifecycleInfo(className, false, state,
                Utils.sdf.format(System.currentTimeMillis())));
    }
}
