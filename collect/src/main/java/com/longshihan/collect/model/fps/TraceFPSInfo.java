package com.longshihan.collect.model.fps;

import com.longshihan.collect.model.TraceOriginInfo;

import java.io.Serializable;

/**
 * 记录APP帧率
 */
public class TraceFPSInfo extends TraceOriginInfo implements Serializable {
    private int fpscount;
    private String datetime;

    public TraceFPSInfo(int fpscount, String datetime) {
        this.fpscount = fpscount;
        this.datetime = datetime;
        dataType = "fps";
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
