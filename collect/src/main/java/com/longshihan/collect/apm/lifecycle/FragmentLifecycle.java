package com.longshihan.collect.apm.lifecycle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.longshihan.collect.control.TraceControl;

public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {
    public FragmentLifecycle() {
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentViewDestroyed");
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentStopped");
    }

    @Override
    public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentPreAttached(fm, f, context);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPreAttached");
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPaused");
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentDetached");
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentDestroyed");
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentCreated");
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached(fm, f, context);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentAttached");
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentActivityCreated");
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentStarted");
    }

    @Override
    public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentPreCreated(fm, f, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentPreCreated");
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentViewDestroyed");
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        TraceControl.saveFragmentLifeCycleState(f.getClass().getName(), "onFragmentViewCreated");
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
    }
}
