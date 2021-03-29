package com.longshihan.collect.apm.fps.listener;

public interface BeatLifecycle {
    void onStart();

    void onStop();

    boolean isAlive();
}
