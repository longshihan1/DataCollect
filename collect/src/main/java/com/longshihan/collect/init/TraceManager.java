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

import com.longshihan.collect.apm.anr.AnrTrace;
import com.longshihan.collect.apm.fps.FPSPlugin;
import com.longshihan.collect.apm.io.IOPlugin;
import com.longshihan.collect.apm.lifecycle.ActivityLifecycle;
import com.longshihan.collect.http.UploadUtils;
import com.longshihan.collect.ui.LMenu;
import com.longshihan.collect.ui.model.SettingInfo;
import com.longshihan.collect.ui.time.CurrentTimeUtils;
import com.longshihan.collect.utils.FileUtils;
import com.longshihan.collect.utils.data.MMAPUtils;
import com.longshihan.collect.utils.data.SharePreferenceUtils;

/**
 * App启动的时候唤醒，初始化相关数据和建立文件，创建连接
 */
public class TraceManager {
    private static TraceManager instance;
    public static SharePreferenceUtils sharePreferenceUtils;
    public static SettingInfo settingInfo;
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

    public static void init(Context context) {
        try {
            mContext = context;
            CurrentTimeUtils.getInstance().init();
            sharePreferenceUtils = new SharePreferenceUtils(context);
            settingInfo = SettingInfo.Companion.getInfo();
            Config.INSTANCE.setHOST(settingInfo.getIpAdress());
            Log.d("测试", "进来了");
            //绑定生命周期
            if (context instanceof Application) {
                Log.d("测试", "进来了1");
                ((Application) context).registerActivityLifecycleCallbacks(new ActivityLifecycle());
            } else {
                Log.d("测试", "进来了2");
                TraceManager.getInstance().showMenu(context);
            }
            //初始化MMKV
            MMAPUtils.defaultinit(mContext, FileUtils.CheckOtherDate());
            //FPS 启动
            FPSPlugin.INSTANCE.init();
            FPSPlugin.INSTANCE.start();
            //Anr 和 Evil
            AnrTrace.getInstance().init();
            AnrTrace.getInstance().start();
            //IO
            IOPlugin.INSTANCE.init();
            if (settingInfo.getBIO()) {
                IOPlugin.INSTANCE.start();
            }
            //创建本地数据库
            //创建上传线程
            UploadUtils.INSTANCE.init();
            UploadUtils.uploadMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean showMenu(Context context) {
        return showMenu(context, 10);
    }

    private boolean showMenu(Context context, int y) {
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


    public void dimiss() {
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
