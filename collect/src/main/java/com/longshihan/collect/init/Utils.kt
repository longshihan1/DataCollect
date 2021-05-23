package com.longshihan.collect.init

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.longshihan.collect.utils.DeviceUtil
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun dip2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            Resources.getSystem().getDisplayMetrics()
        )
    }

    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        var width = 0
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        width = dm.widthPixels
        return width
    }

    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        var height = 0
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        height = dm.heightPixels
        return height
    }
    @JvmField
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @JvmField
    val sdfTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    @JvmField
    var timefilename: String? = null

    @JvmStatic
    fun getStack(): String? {
        val trace = Throwable().stackTrace
        return getStack(trace)
    }

    fun getStack(trace: Array<StackTraceElement>?): String? {
        return getStack(trace, "", -1)
    }

    @JvmStatic
    fun getStack(trace: Array<StackTraceElement>?, preFixStr: String?, limit: Int): String? {
        var limit = limit
        if (trace == null || trace.size < 3) {
            return ""
        }
        if (limit < 0) {
            limit = Int.MAX_VALUE
        }
        val t = StringBuilder(" \n")
        var i = 3
        while (i < trace.size - 3 && i < limit) {
            t.append(preFixStr)
            t.append("at ")
            t.append(trace[i].className)
            t.append(":")
            t.append(trace[i].methodName)
            t.append("(" + trace[i].lineNumber + ")")
            t.append("\n")
            i++
        }
        return t.toString()
    }

    @JvmStatic
    fun calculateCpuUsage(threadMs: Long, ms: Long): String? {
        if (threadMs <= 0) {
            return if (ms > 1000) "0%" else "100%"
        }
        return if (threadMs >= ms) {
            "100%"
        } else String.format("%.2f", 1f * threadMs / ms * 100) + "%"
    }

    fun isEmpty(str: String?): Boolean {
        return null == str || str == ""
    }

    @JvmStatic
    fun getProcessPriority(pid: Int): IntArray? {
        val name = String.format("/proc/%s/stat", pid)
        var priority = Int.MIN_VALUE
        var nice = Int.MAX_VALUE
        try {
            val content: String = DeviceUtil.getStringFromFile(name).trim()
            val args = content.split(" ").toTypedArray()
            if (args.size >= 19) {
                priority = args[17].trim { it <= ' ' }.toInt()
                nice = args[18].trim { it <= ' ' }.toInt()
            }
        } catch (e: Exception) {
            return intArrayOf(priority, nice)
        }
        return intArrayOf(priority, nice)
    }

    fun formatTime(timestamp: Long): String? {
        return SimpleDateFormat("[yy-MM-dd HH:mm:ss]").format(Date(timestamp))
    }
}