package com.longshihan.collect.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author longshihan
 * @time 2020/7/26
 * @des 处理生命周期的数据
 */
public class SaveTraceLifecycle implements Serializable {
    private List<TraceLifecycleInfo> traceTimes=new ArrayList<>();

    public List<TraceLifecycleInfo> getTraceTimes() {
        return traceTimes;
    }

    public void setTraceTimes(List<TraceLifecycleInfo> traceTimes) {
        this.traceTimes = traceTimes;
    }
}
