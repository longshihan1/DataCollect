package com.longshihan.collect.apm.fps

import android.os.Build
import android.os.Looper
import android.os.MessageQueue
import android.os.SystemClock
import android.util.Log
import android.util.Printer
import com.longshihan.collect.apm.fps.listener.LooperDispatchListener
import com.longshihan.collect.utils.MatrixLog.e
import com.longshihan.collect.utils.MatrixLog.i
import com.longshihan.collect.utils.MatrixLog.v
import com.longshihan.collect.utils.MatrixLog.w
import com.longshihan.collect.utils.reflect.ReflectUtils
import java.util.*

class PrinterObservrt(private val looper: Looper = Looper.getMainLooper()) :
    MessageQueue.IdleHandler {
    init {
        resetPrinter()
        addIdleHandler(looper)
    }

    private val TAG = "LooperMonitor"
    private var isReflectLoggingError = false
    private val listeners = HashSet<LooperDispatchListener>()
    private var lastCheckPrinterTime: Long = 0
    private val CHECK_TIME = 60 * 1000L
    private var printer: LooperPrinter? = null

    companion object {
        private val mainMonitor = PrinterObservrt()

        @JvmStatic
        fun register(listener: LooperDispatchListener) {
            mainMonitor.addListener(listener)
        }

        @JvmStatic
        fun unregister(listener: LooperDispatchListener?) {
            mainMonitor.removeListener(listener)
        }

    }

    override fun queueIdle(): Boolean {
        if (SystemClock.uptimeMillis() - lastCheckPrinterTime >= CHECK_TIME) {
            resetPrinter()
            lastCheckPrinterTime = SystemClock.uptimeMillis()
        }
        return true
    }

    fun getListeners(): HashSet<LooperDispatchListener> {
        return listeners
    }

    fun addListener(listener: LooperDispatchListener) {
        synchronized(listeners) { listeners.add(listener) }
    }

    fun removeListener(listener: LooperDispatchListener?) {
        synchronized(listeners) { listeners.remove(listener) }
    }

    @Synchronized
    private fun resetPrinter() {
        var originPrinter: Printer? = null
        try {
            if (!isReflectLoggingError) {
                originPrinter = ReflectUtils.get<Printer>(looper.javaClass, "mLogging", looper)
                if (originPrinter === printer && null != printer) {
                    return
                }
            }
        } catch (e: Exception) {
            isReflectLoggingError = true
            Log.e(TAG, "[resetPrinter] %s", e)
        }
        if (null != printer) {
            w(
                TAG,
                "maybe thread:%s printer[%s] was replace other[%s]!",
                looper.thread.name,
                printer,
                originPrinter
            )
        }
        looper.setMessageLogging(LooperPrinter(originPrinter).also {
            printer = it
        })
        if (originPrinter != null) {

            i(TAG, "reset printer, originPrinter[%s] in %s", originPrinter, looper.thread.name)
        }
    }

    @Synchronized
    private fun addIdleHandler(looper: Looper) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            looper.queue.addIdleHandler(this)
        } else {
            try {
                val queue = ReflectUtils.get<MessageQueue>(looper.javaClass, "mQueue", looper)
                queue.addIdleHandler(this)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "[removeIdleHandler] %s", e)
            }
        }
    }


    @Synchronized
    fun onRelease() {
        if (printer != null) {
            synchronized(listeners) { listeners.clear() }
            v(
                TAG,
                "[onRelease] %s, origin printer:%s",
                looper.thread!!.name,
                printer!!.origin
            )
            looper.setMessageLogging(printer!!.origin)
            removeIdleHandler(looper!!)
            printer = null
        }
    }

    @Synchronized
    private fun removeIdleHandler(looper: Looper) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            looper.queue.removeIdleHandler(this)
        } else {
            try {
                val queue = ReflectUtils.get<MessageQueue>(looper.javaClass, "mQueue", looper)
                queue.removeIdleHandler(this)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "[removeIdleHandler] %s", e)
            }
        }
    }

    inner class LooperPrinter(var origin: Printer?) : Printer {
        var isHasChecked = false
        var isValid = false
        override fun println(x: String) {
            if (null != origin) {
                origin?.println(x)
                if (origin === this) {
                    throw RuntimeException(this@PrinterObservrt.TAG + " origin == this")
                }
            }
            if (!isHasChecked) {
                isValid = x[0] == '>' || x[0] == '<'
                isHasChecked = true
                if (!isValid) {
                    e(this@PrinterObservrt.TAG, "[println] Printer is inValid! x:%s", x)
                }
            }
            if (isValid) {
                this@PrinterObservrt.dispatch(x[0] == '>', x)
            }
        }
    }


    private fun dispatch(isBegin: Boolean, log: String) {
        for (listener in listeners) {
            if (listener.isValid) {
                if (isBegin) {
                    if (!listener.isHasDispatchStart) {
                        listener.onDispatchStart(log)
                    }
                } else {
                    if (listener.isHasDispatchStart) {
                        listener.onDispatchEnd(log)
                    }
                }
            } else if (!isBegin && listener.isHasDispatchStart) {
                listener.dispatchEnd()
            }
        }
    }
}