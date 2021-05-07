package com.longshihan.collect.utils

import android.content.Context
import com.longshihan.collect.init.Utils

/**
 * 处理IO异常的日志文件
 */
object IOSPUtils {
    var iOfileId = 0L;

    /**
     * 默认的文件读写使用
     */
    @JvmStatic
    fun defaultIOinit(context: Context, fileName: String) {
        iOfileId = SPUtils.init(context, fileName + "_io")
    }

    /**
     * 默认方式下的文件存储
     */
    @JvmStatic
    fun saveIOValue(value: String) {
        val tempStr= Utils.sdf.format(System.currentTimeMillis())+value+"/n"
        SPUtils.save(iOfileId, tempStr)
    }

}