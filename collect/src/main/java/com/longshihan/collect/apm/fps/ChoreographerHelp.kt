package com.longshihan.collect.apm.fps

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.view.Choreographer
import com.longshihan.collect.apm.fps.listener.FpsObserver
import com.longshihan.collect.apm.fps.listener.LooperObserver
import com.longshihan.collect.control.TraceControl
import com.longshihan.collect.utils.MatrixLog.d
import java.util.*

/**
 * fps 相关总类
 */
object ChoreographerHelp : Choreographer.FrameCallback {
    var mFpsCount = 0
    private var mLastFrameTimeNanos: Long = 0 //最后一次时间
    private var mFrameTimeNanos: Long = 0 //本次的当前时间
    private var observers :FpsObserver?=null
    fun start() {
        //启动一秒循环器的数据
        handler.sendEmptyMessage(1)
        Choreographer.getInstance().postFrameCallback(this)
    }

    fun addObserver(observer: FpsObserver) {
        observers=observer
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
//                    d("FPS", "当前帧率：$fpsResult", "")
                    if (fpsResult < 0) {
                        return
                    }
                    //添加数据
                    observers?.frameCallback(fpsResult.toInt())
                }
                if (observers != null) {
                    sendEmptyMessageDelayed(1, 1000)
                }
            }
        }
    }

    override fun doFrame(frameTimeNanos: Long) {
        mFrameTimeNanos = frameTimeNanos
        mFpsCount++
        Choreographer.getInstance().postFrameCallback(this)
    }
}