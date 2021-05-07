package com.longshihan.collect.utils

import android.util.Log

object MatrixLog {
    private val debugLog: MatrixLogImp = object : MatrixLogImp {
        override fun v(tag: String?, format: String?, vararg params: Any?) {
            val log = if (params != null && params.isNotEmpty()) String.format(
                format!!,
                *params
            ) else format!!
            Log.v(tag, log)
        }

        override fun i(tag: String?, format: String?, vararg params: Any?) {
            val log = if (params != null && params.isNotEmpty()) String.format(
                format!!,
                *params
            ) else format!!
            Log.i(tag, log)
        }

        override fun d(tag: String?, format: String?, vararg params: Any?) {
            val log = if (params != null && params.isNotEmpty()) String.format(
                format!!,
                *params
            ) else format!!
            Log.d(tag, log)
        }

        override fun w(tag: String?, format: String?, vararg params: Any?) {
            val log = if (params != null && params.isNotEmpty()) String.format(
                format!!,
                *params
            ) else format!!
            Log.w(tag, log)
        }

        override fun e(tag: String?, format: String?, vararg params: Any?) {
            val log = if (params != null && params.isNotEmpty()) String.format(
                format!!,
                *params
            ) else format!!
            Log.e(tag, log)
        }

        override fun printErrStackTrace(
            tag: String?,
            tr: Throwable?,
            format: String?,
            vararg params: Any?
        ) {
            var log =
                if (params != null && params.isNotEmpty()) String.format(
                    format!!,
                    *params
                ) else format
            if (log == null) {
                log = ""
            }
            log = log + "  " + Log.getStackTraceString(tr)
            Log.e(tag, log)
        }
    }
    private var matrixLogImp: MatrixLogImp? = debugLog

    private fun MatrixLog() {}

    fun setMatrixLogImp(imp: MatrixLogImp?) {
        matrixLogImp = imp
    }

    fun getImpl(): MatrixLogImp? {
        return matrixLogImp
    }

    @JvmStatic
    fun v(tag: String?, msg: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.v(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun e(tag: String?, msg: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.e(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun w(tag: String?, msg: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.w(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun i(tag: String?, msg: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.i(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun d(tag: String?, msg: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.d(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun printErrStackTrace(tag: String?, tr: Throwable?, format: String?, vararg obj: Any?) {
        if (matrixLogImp != null) {
            matrixLogImp!!.printErrStackTrace(tag, tr, format, *obj)
        }
    }

    interface MatrixLogImp {
        fun v(var1: String?, var2: String?, vararg var3: Any?)
        fun i(var1: String?, var2: String?, vararg var3: Any?)
        fun w(var1: String?, var2: String?, vararg var3: Any?)
        fun d(var1: String?, var2: String?, vararg var3: Any?)
        fun e(var1: String?, var2: String?, vararg var3: Any?)
        fun printErrStackTrace(var1: String?, var2: Throwable?, var3: String?, vararg var4: Any?)
    }
}