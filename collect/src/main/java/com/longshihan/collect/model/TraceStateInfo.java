package com.longshihan.collect.model;

import java.io.Serializable;

/**
 * 记录APP其他时间
 */
public class TraceStateInfo implements Serializable {
    private String tag;//前台后台，开屏息屏等
    private String state;
    private String datetime;

    public TraceStateInfo(String tag, String state, String datetime) {
        this.tag = tag;
        this.state = state;
        this.datetime = datetime;
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
}
