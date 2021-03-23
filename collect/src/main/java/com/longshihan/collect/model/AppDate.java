package com.longshihan.collect.model;

import com.longshihan.collect.model.fps.TraceFPSInfo;
import com.longshihan.collect.model.lifecycle.TraceLifecycleInfo;
import com.longshihan.collect.model.time.TraceTimeInfo;
import com.longshihan.collect.utils.JsonUtils;
import com.longshihan.collect.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppDate implements Serializable {
    public List<TraceFPSInfo> traceFPSInfos = new ArrayList<>();
    public List<TraceLifecycleInfo> traceLifecycleInfos = new ArrayList<>();
    public List<TraceTimeInfo> traceTimeInfos = new ArrayList<>();

    public AppDate(List<TraceFPSInfo> traceFPSInfos,
                   List<TraceLifecycleInfo> traceLifecycleInfos,
                   List<TraceTimeInfo> traceTimeInfos) {
        this.traceFPSInfos.addAll(traceFPSInfos);
        this.traceLifecycleInfos.addAll(traceLifecycleInfos);
        this.traceTimeInfos.addAll(traceTimeInfos);
    }

    public void saveSp() {
        SPUtils.appendTrace(JsonUtils.INSTANCE.getGosn().toJson(traceFPSInfos));
        SPUtils.appendTrace(JsonUtils.INSTANCE.getGosn().toJson(traceLifecycleInfos));
        SPUtils.appendTrace(JsonUtils.INSTANCE.getGosn().toJson(traceTimeInfos));
    }

    public void clear() {
        traceFPSInfos.clear();
        traceLifecycleInfos.clear();
        traceTimeInfos.clear();
    }
}
