package com.longshihan.collect.apm.fps.listener

import android.support.annotation.CallSuper

abstract class LooperObserver {
    private var isDispatchBegin = false

    @CallSuper
    open fun dispatchBegin(beginNs: Long, cpuBeginNs: Long, token: Long) {
        isDispatchBegin = true
    }

    open fun doFrame(
        focusedActivity: String?,
        startNs: Long,
        endNs: Long,
        isVsyncFrame: Boolean,
        intendedFrameTimeNs: Long,
        inputCostNs: Long,
        animationCostNs: Long,
        traversalCostNs: Long
    ) {
    }

    @CallSuper
    open fun dispatchEnd(
        beginNs: Long,
        cpuBeginMs: Long,
        endNs: Long,
        cpuEndMs: Long,
        token: Long,
        isVsyncFrame: Boolean
    ) {
        isDispatchBegin = false
    }

    open fun isDispatchBegin(): Boolean {
        return isDispatchBegin
    }
}