package com.longshihan.collect.ui.time;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

public class CurrentTimeUtils {
    public static long currentTime=System.currentTimeMillis();
    private static CurrentTimeUtils instance;
   private CurrentHandler handler;
    public CurrentTimeUtils(){
        handler=new CurrentHandler();
    }
    public static CurrentTimeUtils getInstance() {
        if (instance==null){
            instance=new CurrentTimeUtils();
        }
        return instance;
    }

    public  void init(){
        handler.sendEmptyMessage(1000);
    }

    public void destory(){
        handler.removeMessages(1000);
    }

    public static class CurrentHandler extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            currentTime=System.currentTimeMillis();
            sendEmptyMessageDelayed(1000,5);
        }
    }
}
