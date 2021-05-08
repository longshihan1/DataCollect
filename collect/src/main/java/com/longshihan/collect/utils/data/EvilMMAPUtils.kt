package com.longshihan.collect.utils.data

import android.content.Context
import com.longshihan.collect.init.Utils

/**
 * 处理慢方法异常的日志文件
 */
object EvilMMAPUtils {

    var evilfileId = 0L;

    /**
     * 默认的文件读写使用
     */
    @JvmStatic
    fun defaultEvilinit(context: Context, fileName: String) {
        evilfileId = MMAPUtils.init(context, fileName + "_evil")
    }

    /**
     * 默认方式下的文件存储
     */
    @JvmStatic
    fun saveEvilValue(value: String) {
        val tempStr= Utils.sdf.format(System.currentTimeMillis())+value+"/n"
        MMAPUtils.save(evilfileId, value)
    }

}