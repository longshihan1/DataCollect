package com.longshihan.collect.init;

import com.longshihan.collect.traceTime.TraceTime;

/**
 * 插桩的入口调用
 */
public class Trace {

    public static void initFirst(String clazzname,String methodName){
        TraceTime.saveLast(clazzname, methodName);
    }

    public static void initLast(String clazzname,String methodName){
        TraceTime.saveFirst(clazzname, methodName);

    }
}
