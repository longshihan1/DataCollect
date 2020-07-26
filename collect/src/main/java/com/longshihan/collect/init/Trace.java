package com.longshihan.collect.init;

import com.longshihan.collect.traceTime.TraceTime;
import com.longshihan.collect.utils.IDCreator;

/**
 * 插桩的入口调用
 */
public class Trace {

    public static void initFirst(String clazzname,String methodName){
        //获取当前线程，进程，时间,生成唯一id，方法调用
        String id=IDCreator.INSTANCE.getID();
        //将id绑定调用链
        TraceTime.saveLast(clazzname, methodName);
    }

    public static void initLast(String clazzname,String methodName){

        TraceTime.saveFirst(clazzname, methodName);

    }
}
