package com.longshihan.collect.traceTime;

import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.time.TraceTimeInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TraceTime {
    public static List<TraceTimeInfo> traceTimeInfos = new ArrayList<>();
    public static Map<String, Long> sStartTime = new HashMap<>();

    public static void saveFirst(String clazzname, String methodName,String tag) {
        sStartTime.put(tag, System.currentTimeMillis());
    }

    public static void saveLast(String clazzname, String methodName,String tag) {
        Long startTime = sStartTime.get(tag);
        if (startTime==null){
            startTime=System.currentTimeMillis();
        }
        long currentTime=System.currentTimeMillis() - startTime;

        traceTimeInfos.add(new TraceTimeInfo(UUID.randomUUID().toString()
                , clazzname +"."+ methodName, currentTime
                , Utils.sdf.format(System.currentTimeMillis()), Thread.currentThread().getName()));
    }

}
