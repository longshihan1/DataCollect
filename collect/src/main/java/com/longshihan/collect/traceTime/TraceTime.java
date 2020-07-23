package com.longshihan.collect.traceTime;

import com.longshihan.collect.init.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class TraceTime {
    private static StringBuilder stringBuilder=new StringBuilder();
    static int count=0;
    public static Map<String, Long> sStartTime = new HashMap<>();
    public static void saveFirst(String clazzname,String methodName){
        sStartTime.put(clazzname+methodName,System.currentTimeMillis());
        count++;
    }

    public static void saveLast(String clazzname,String methodName){
        long  startTime=sStartTime.get(clazzname+methodName);
        startTime=System.currentTimeMillis();
        stringBuilder.append(clazzname+"-"+methodName +",coast:"+(System.currentTimeMillis()-startTime)+"\n");
        count++;
        if (count%30==1){
            saveData();
        }
    }

    public static void saveData(){
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(new File(Utils.timefilename),true);
            FileChannel channel=fos.getChannel();
            ByteBuffer src= Charset.forName("utf8").encode(stringBuilder.toString());
            int length=0;
            while (channel.write(src)!=0){ }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            count=0;
            stringBuilder.delete(0,stringBuilder.length());
            try {
                if (fos!=null) {
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
