package com.longshihan.collect.init;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longshihan
 * @time 2020/7/26
 * 监听activity的生命周期
 * 在里面做popu
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private List<Activity> mActivitys=new ArrayList<>();
    private int count=0;
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mActivitys.add(activity);
        if (mActivitys.size()==1){//第一次启动
            TraceManager.getInstance().showMenu(activity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        count++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        count--;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        mActivitys.remove(activity);
    }
}
