package com.longshihan.collect.init;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.longshihan.collect.http.UploadUtils;

import java.io.File;

/**
 * App启动的时候唤醒，初始化相关数据和建立文件，创建连接
 */
public class TraceManager {
    private static TraceManager instance;
    private LMenu menu;
    public static Context mContext;

    private TraceManager() {
    }

   public static TraceManager getInstance() {
        if (instance == null) {
            synchronized (TraceManager.class) {
                if (instance == null) {
                    instance = new TraceManager();
                }
            }
        }
        return instance;
    }
    public static void init(Context context){
        try {
            mContext=context;
            Log.d("测试","进来了");
            //绑定生命周期
            if (context instanceof Application){
                Log.d("测试","进来了1");
            ((Application)context).registerActivityLifecycleCallbacks(new ActivityLifecycle());
            }else {
                Log.d("测试","进来了2");
                TraceManager.getInstance().showMenu(context);
            }
            //创建本地数据库
            //创建上传线程
            UploadUtils.INSTANCE.init();
            //轮训开启任务
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

    public boolean showMenu(Context context) {
        return showMenu(context,10);
    }

    private boolean showMenu(Context context,int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                requestPermission(context);
                Toast.makeText(context, "After grant this permission, re-enable UETool", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (menu == null) {
            menu = new LMenu(context, y);
        }
        if (!menu.isShown()) {
            menu.show();
            return true;
        }
        return false;
    }


    public void dimiss(){
        if (menu != null) {
            menu.dismiss();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
