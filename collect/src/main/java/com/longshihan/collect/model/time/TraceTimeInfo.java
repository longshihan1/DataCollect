package com.longshihan.collect.model.time;

import com.longshihan.collect.model.TraceOriginInfo;

import java.io.Serializable;

/**
 * @author longshihan
 * @time 2020/7/26
 */
public class TraceTimeInfo extends TraceOriginInfo implements Serializable {
    private String tag;//唯一标识符
    private String methodName;
    private long cost;
    private String datetime;
    private String threadName;

    public TraceTimeInfo() {
    }

    public TraceTimeInfo(String tag, String methodName, long cost, String datetime, String threadName) {
        this.tag = tag;
        this.methodName = methodName;
        this.cost = cost;
        this.datetime = datetime;
        this.threadName = threadName;
        dataType = "time";
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getCost() {
        return cost;
    }

    public TraceTimeInfo setCost(long cost) {
        this.cost = cost;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Object[] getObjects() {
        return new Object[]{getTag(), getMethodName(), getCost(), getDatetime()};
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
