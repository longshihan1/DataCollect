package com.longshihan.collect.model.lifecycle;

import java.io.Serializable;

/**
 * 记录APP生命周期活动
 */
public class TraceLifecycleInfo implements Serializable {
    private String activityName;
    private boolean isActivity;
    private String state;
    private String datetime;

    public TraceLifecycleInfo(String activityName, boolean isActivity, String state, String datetime) {
        this.activityName = activityName;
        this.isActivity = isActivity;
        this.state = state;
        this.datetime = datetime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }
}
