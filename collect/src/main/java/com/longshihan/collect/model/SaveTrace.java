package com.longshihan.collect.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author longshihan
 * @time 2020/7/26
 *
 */
public class SaveTrace implements Serializable {
    private List<TraceTimeInfo> traceTimes=new ArrayList<>();

    public List<TraceTimeInfo> getTraceTimes() {
        return traceTimes;
    }

    public void setTraceTimes(List<TraceTimeInfo> traceTimes) {
        this.traceTimes = traceTimes;
    }
}
