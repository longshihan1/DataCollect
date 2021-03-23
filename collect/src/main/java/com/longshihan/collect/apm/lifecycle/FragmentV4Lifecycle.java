package com.longshihan.collect.apm.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.longshihan.collect.control.TraceControl;


public class FragmentV4Lifecycle extends FragmentManager.FragmentLifecycleCallbacks {
    public FragmentV4Lifecycle() {
    }

    @Override
    public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentActivityCreated");
    }

    @Override
    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        super.onFragmentAttached(fm, f, context);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentAttached");
    }

    @Override
    public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentCreated");
    }

    @Override
    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDestroyed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentDestroyed");
    }

    @Override
    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDetached(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentDetached");
    }

    @Override
    public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentPaused(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPaused");
    }

    @Override
    public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        super.onFragmentPreAttached(fm, f, context);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPreAttached");
    }

    @Override
    public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentPreCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPreCreated");
    }

    @Override
    public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentResumed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentResumed");
    }

    @Override
    public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentStarted(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentStarted");
    }

    @Override
    public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentStopped(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentStopped");
    }

    @Override
    public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentViewCreated");
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentViewDestroyed");
    }
}

