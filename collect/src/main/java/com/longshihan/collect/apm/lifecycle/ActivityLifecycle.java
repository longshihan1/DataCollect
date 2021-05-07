package com.longshihan.collect.apm.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.longshihan.collect.control.TraceControl;
import com.longshihan.collect.init.TraceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longshihan
 * @time 2020/7/26
 * 监听activity的生命周期
 * 在里面做popu
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private List<Activity> mActivitys = new ArrayList<>();
    private int count = 0;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mActivitys.add(activity);
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityCreated");
        if (mActivitys.size() == 1) {//第一次启动
            TraceManager.getInstance().showMenu(activity);
        }

        activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycle(), true);
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentV4Lifecycle(), true);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        count++;
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityStarted");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityResumed");

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityPaused");

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityStopped");
        count--;
        if (count == 0) {
            TraceManager.getInstance().dimiss();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        TraceControl.saveActivityLifeCycleState(activity.getClass().getName(), "onActivityDestroyed");
        mActivitys.remove(activity);
    }
}
