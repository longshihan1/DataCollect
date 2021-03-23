package com.longshihan.collect.apm.fps

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.view.Choreographer
import com.longshihan.collect.control.TraceControl


class ChoreographerHelp : Choreographer.FrameCallback {
    var mFpsCount = 0
    private var mLastFrameTimeNanos: Long = 0 //最后一次时间
    private var mFrameTimeNanos: Long = 0 //本次的当前时间

    init {
        //启动
        start()
    }

    private fun start() {
        //启动一秒循环器的数据
        handler.sendEmptyMessage(1)
        Choreographer.getInstance().postFrameCallback(this)
    }

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val currentFpsCount: Int = mFpsCount
                mFpsCount = 0
                if (this@ChoreographerHelp.mLastFrameTimeNanos == 0L) {
                    mLastFrameTimeNanos = mFrameTimeNanos;
                } else {
                    val costTime = (mFrameTimeNanos - mLastFrameTimeNanos).toFloat() / 1000000.0f
                    mLastFrameTimeNanos = mFrameTimeNanos
                    if (currentFpsCount <= 0 && costTime <= 0.0f) {
                        return
                    }
                    val fpsResult = (currentFpsCount * 1000 / costTime).toDouble()
                    if (fpsResult < 0) {
                        return
                    }
                    //添加数据
                    TraceControl.saveFps(fpsResult.toInt())
                }
                sendEmptyMessageDelayed(1, 1000)
            }
        }
    }

    override fun doFrame(frameTimeNanos: Long) {
        mFrameTimeNanos = frameTimeNanos
        mFpsCount++
        Choreographer.getInstance().postFrameCallback(this)
    }
}