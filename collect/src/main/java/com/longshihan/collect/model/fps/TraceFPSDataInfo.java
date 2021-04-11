package com.longshihan.collect.model.fps;

import com.longshihan.collect.model.TraceOriginInfo;

import java.io.Serializable;

/**
 * 记录APP帧率 详细数据
 */
public class TraceFPSDataInfo extends TraceOriginInfo implements Serializable {
    private long inputCostNs;
    private long animationCostNs;
    private long traversalCostNs;
    private String datetime;

    public TraceFPSDataInfo(long inputCostNs,long animationCostNs,long traversalCostNs, String datetime) {
        this.inputCostNs = inputCostNs;
        this.animationCostNs = animationCostNs;
        this.traversalCostNs = traversalCostNs;
        this.datetime = datetime;
        dataType = "fpsData";
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getInputCostNs() {
        return inputCostNs;
    }

    public void setInputCostNs(long inputCostNs) {
        this.inputCostNs = inputCostNs;
    }

    public long getAnimationCostNs() {
        return animationCostNs;
    }

    public void setAnimationCostNs(long animationCostNs) {
        this.animationCostNs = animationCostNs;
    }

    public long getTraversalCostNs() {
        return traversalCostNs;
    }

    public void setTraversalCostNs(long traversalCostNs) {
        this.traversalCostNs = traversalCostNs;
    }
}
