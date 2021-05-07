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
    public List<TraceOriginInfo> dataList = new ArrayList<>();

    public AppDate(List<TraceFPSInfo> traceFPSInfos,
                   List<TraceLifecycleInfo> traceLifecycleInfos,
                   List<TraceTimeInfo> traceTimeInfos) {
        dataList.addAll(traceFPSInfos);
        dataList.addAll(traceLifecycleInfos);
        dataList.addAll(traceTimeInfos);
    }

    public void saveSp() {
        SPUtils.appendTrace(JsonUtils.INSTANCE.getGosn().toJson(dataList));
    }

    public void clear() {
        dataList.clear();
    }
}
