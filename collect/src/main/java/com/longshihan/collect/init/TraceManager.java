package com.longshihan.collect.init;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * App启动的时候唤醒，初始化相关数据和建立文件，创建连接
 */
public class TraceManager {
    public static void init(Context context){
        try {
            Log.d("测试","进来了");
            File file=context.getExternalCacheDir();
            if (!file.exists()){
                file.mkdirs();
            }
            Utils.timefilename=file.getAbsolutePath()+"/time-"+Utils.sdf.format(System.currentTimeMillis())+ ".txt";
            File file1=new File(Utils.timefilename);
            if (!file1.exists()){
                file1.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
