package com.longshihan.collect.apm.io

import com.longshihan.collect.apm.io.core.IOCanaryCore
import com.longshihan.collect.init.TraceManager
import com.longshihan.collect.plugin.IPlugin
import com.longshihan.collect.utils.FileUtils.Companion.CheckOtherDate
import com.longshihan.collect.utils.IOCanaryUtil
import com.longshihan.collect.utils.data.IOMMAPUtils

object IOPlugin : IPlugin {
    var mCore: IOCanaryCore? = null
    override fun init() {
        IOMMAPUtils.defaultIOinit(TraceManager.mContext, CheckOtherDate())
        IOCanaryUtil.setPackageName(TraceManager.mContext)
        mCore = IOCanaryCore(this)
    }

    override fun start() {
        mCore?.start()
    }

    override fun stop() {
        mCore?.stop()
    }

    override fun destory() {

    }
}