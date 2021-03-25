package com.longshihan.collect.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.longshihan.mmap.MMAP

/**
 * 文件存储相关
 */
class SPUtils {
    companion object {
        var fileId = 0L;

        /**
         * 可以自己控制文件读写
         */
        @JvmStatic
        fun init(context: Context, fileName: String): Long {
            return MMAP.nativeInit(context.externalCacheDir?.absolutePath, fileName)
        }

        /**
         * 默认的文件读写使用
         */
        @JvmStatic
        fun defaultinit(context: Context, fileName: String) {
            fileId = MMAP.nativeInit(context.externalCacheDir?.absolutePath, fileName)
        }
        @JvmStatic
        fun save(fileId: Long, value: String) {
            MMAP.nativeWrite(fileId, "$value\r\n")
        }

        /**
         * 默认方式下的文件存储
         */
        @JvmStatic
        fun save(value: String) {
            if (fileId > 0) {
                save(fileId, value)
            } else {
                Log.d("Longshihan_Collect", "mmap未初始化")
            }
        }

        @JvmStatic
        fun appendTrace(value: String) {
            save(value)
        }

    }
}