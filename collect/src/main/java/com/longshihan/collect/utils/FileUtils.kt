package com.longshihan.collect.utils

class FileUtils {
    companion object {
        @JvmStatic
        fun CheckOtherDate(): String {
            return "Longshihan_collect_" + System.currentTimeMillis()
        }
    }
}