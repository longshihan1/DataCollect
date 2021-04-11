package com.longshihan.collect.plugin

/**
 * 插件统一管理生命周期
 */
interface IPlugin {
    fun init()
    fun start()
    fun stop()
    fun destory()
}