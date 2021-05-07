package com.longshihan.collect.traceTime;

import com.longshihan.collect.model.time.TraceTimeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceTime {
    public static List<TraceTimeInfo> traceTimeInfos = new ArrayList<>();
    public static Map<String, Long> sStartTime = new HashMap<>();

    public static List<String> currenttraceTime = new ArrayList<>();

    public static void saveFirst(String clazzname, String methodName, String tag) {
//        sStartTime.put(tag, System.currentTimeMillis());
        currenttraceTime.add(clazzname + ":" + methodName);
    }

    public static void saveLast(String clazzname, String methodName, String tag) {
//        Long startTime = sStartTime.get(tag);
//        if (startTime==null){
//            startTime=System.currentTimeMillis();
//        }
//        long currentTime=System.currentTimeMillis() - startTime;
//
//        traceTimeInfos.add(new TraceTimeInfo(UUID.randomUUID().toString()
//                , clazzname +"."+ methodName, currentTime
//                , Utils.sdf.format(System.currentTimeMillis()), Thread.currentThread().getName()));
    }

    public static void clear() {
        traceTimeInfos.clear();
    }

    public static void onFrameStart() {
        currenttraceTime.clear();
        tempList.clear();
    }

    static List<String> tempList = new ArrayList<>();

    public static void onFrameEnd() {
        tempList.addAll(currenttraceTime);
        currenttraceTime.clear();
    }

    public static List<String> onFrameStack() {
        return tempList;
    }

}
