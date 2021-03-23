package com.longshihan.collect.model.fps;

import java.io.Serializable;

/**
 * 记录APP帧率
 */
public class TraceFPSInfo implements Serializable {
    private int fpscount;
    private String datetime;

    public TraceFPSInfo(int fpscount,  String datetime) {
        this.fpscount = fpscount;
        this.datetime = datetime;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getFpscount() {
        return fpscount;
    }

    public void setFpscount(int fpscount) {
        this.fpscount = fpscount;
    }
}
