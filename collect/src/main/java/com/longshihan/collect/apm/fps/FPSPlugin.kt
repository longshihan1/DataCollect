package com.longshihan.collect.apm.fps

import com.longshihan.collect.apm.fps.listener.FpsObserver
import com.longshihan.collect.apm.fps.listener.LooperObserver
import com.longshihan.collect.control.TraceControl
import com.longshihan.collect.plugin.IPlugin
import com.longshihan.collect.utils.MatrixLog

/**
 * FPS插件管理处
 * 主要处理 FPS帧率回调
 *          帧率三个时间
 *          message处理时间
 */
object FPSPlugin : IPlugin {
    override fun init() {
        try {
            UIThreadMonitor.getMonitor().init()
        } catch (e: RuntimeException) {
            MatrixLog.e("UIThreadMonitor", "[start] RuntimeException:%s", e)
        }
    }

    override fun start() {
        //处理回调数据
        initDataListener()
        //处理fps帧率数据
        if (!UIThreadMonitor.getMonitor().isInit) {
            try {
                UIThreadMonitor.getMonitor().init()
            } catch (e: RuntimeException) {
                MatrixLog.e("UIThreadMonitor", "[start] RuntimeException:%s", e)
            }
        }
        UIThreadMonitor.getMonitor().onStart()
        //处理massage耗时数据

        //处理帧率回调
        ChoreographerHelp.start()
    }

    private fun initDataListener() {
        //处理fps帧率数据
        UIThreadMonitor.getMonitor().addObserver(object : LooperObserver() {
            override fun doFrame(
                focusedActivity: String?, startNs: Long, endNs: Long, isVsyncFrame: Boolean,
                intendedFrameTimeNs: Long, inputCostNs: Long, animationCostNs: Long,
                traversalCostNs: Long
            ) {
                super.doFrame(
                    focusedActivity, startNs, endNs, isVsyncFrame, intendedFrameTimeNs,
                    inputCostNs, animationCostNs, traversalCostNs
                )
                TraceControl.saveFpsData(inputCostNs, animationCostNs, traversalCostNs)
            }

            override fun dispatchBegin(beginNs: Long, cpuBeginNs: Long, token: Long) {
                super.dispatchBegin(beginNs, cpuBeginNs, token)
            }

            override fun dispatchEnd(
                beginNs: Long,
                cpuBeginMs: Long,
                endNs: Long,
                cpuEndMs: Long,
                token: Long,
                isVsyncFrame: Boolean
            ) {
                super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isVsyncFrame)
            }
        })

        ChoreographerHelp.addObserver(object : FpsObserver() {
            override fun frameCallback(count: Int) {
                TraceControl.saveFps(count)
            }
        })


    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun destory() {
        TODO("Not yet implemented")
    }
}