package com.longshihan.collect.apm.fps;

import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.fps.TraceFPSInfo;
import com.longshihan.collect.model.lifecycle.TraceLifecycleInfo;

import java.util.ArrayList;
import java.util.List;

public class TraceFPSControl {
    public static List<TraceFPSInfo> traceFPSInfos = new ArrayList<>();

    public static void saveActivityLifeCycleState(int fps) {
        traceFPSInfos.add(new TraceFPSInfo(fps,
                Utils.sdf.format(System.currentTimeMillis())));
    }
}
