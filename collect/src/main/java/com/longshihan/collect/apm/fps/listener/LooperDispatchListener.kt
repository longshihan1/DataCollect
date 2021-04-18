package com.longshihan.collect.apm.fps.listener

import android.support.annotation.CallSuper

abstract class LooperDispatchListener {
    var isHasDispatchStart = false
    open val isValid: Boolean
        get() = false

    open fun dispatchStart() {}

    @CallSuper
    open  fun onDispatchStart(x: String?) {
        isHasDispatchStart = true
        dispatchStart()
    }

    @CallSuper
    open  fun onDispatchEnd(x: String?) {
        isHasDispatchStart = false
        dispatchEnd()
    }

    open fun dispatchEnd() {}
}