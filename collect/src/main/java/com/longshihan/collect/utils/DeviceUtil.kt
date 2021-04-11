package com.longshihan.collect.utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Debug
import android.os.Process
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.regex.Pattern

object DeviceUtil {
    private const val MB = (1024 * 1024).toLong()
    private const val TAG = "Matrix.DeviceUtil"
    private const val INVALID = 0
    private const val MEMORY_FILE_PATH = "/proc/meminfo"
    private const val CPU_FILE_PATH_0 = "/sys/devices/system/cpu/"
    private const val CPU_FILE_PATH_1 = "/sys/devices/system/cpu/possible"
    private const val CPU_FILE_PATH_2 = "/sys/devices/system/cpu/present"
    private var sLevelCache: LEVEL? = null

    const val DEVICE_MACHINE = "machine"
    private const val DEVICE_MEMORY_FREE = "mem_free"
    private const val DEVICE_MEMORY = "mem"
    private const val DEVICE_CPU = "cpu_app"

    private var sTotalMemory: Long = 0
    private var sLowMemoryThresold: Long = 0
    private var sMemoryClass = 0

    enum class LEVEL(var value: Int) {
        BEST(5), HIGH(4), MIDDLE(3), LOW(2), BAD(1), UN_KNOW(-1);

    }

    fun getDeviceInfo(oldObj: JSONObject, context: Application): JSONObject? {
        try {
            oldObj.put(DEVICE_MACHINE, getLevel(context))
            oldObj.put(DEVICE_CPU, getAppCpuRate())
            oldObj.put(DEVICE_MEMORY, getTotalMemory(context))
            oldObj.put(DEVICE_MEMORY_FREE, getMemFree(context))
        } catch (e: JSONException) {
            MatrixLog.e(TAG, "[JSONException for stack, error: %s", e)
        }
        return oldObj
    }

    fun getLevel(context: Context): LEVEL? {
        if (null != sLevelCache) {
            return sLevelCache
        }
        val start = System.currentTimeMillis()
        val totalMemory = getTotalMemory(context)
        val coresNum = getNumOfCores()
        MatrixLog.i(TAG, "[getLevel] totalMemory:%s coresNum:%s", totalMemory, coresNum)
        if (totalMemory >= 8 * 1024 * MB) {
            sLevelCache = LEVEL.BEST
        } else if (totalMemory >= 6 * 1024 * MB) {
            sLevelCache = LEVEL.HIGH
        } else if (totalMemory >= 4 * 1024 * MB) {
            sLevelCache = LEVEL.MIDDLE
        } else if (totalMemory >= 2 * 1024 * MB) {
            if (coresNum >= 4) {
                sLevelCache = LEVEL.MIDDLE
            } else if (coresNum >= 2) {
                sLevelCache = LEVEL.LOW
            } else if (coresNum > 0) {
                sLevelCache = LEVEL.LOW
            }
        } else if (0 <= totalMemory && totalMemory < 1024 * MB) {
            sLevelCache = LEVEL.BAD
        } else {
            sLevelCache = LEVEL.UN_KNOW
        }
        MatrixLog.i(
            TAG,
            "getLevel, cost:" + (System.currentTimeMillis() - start) + ", level:" + sLevelCache
        )
        return sLevelCache
    }

    private fun getAppId(): Int {
        return Process.myPid()
    }

    fun getLowMemoryThresold(context: Context): Long {
        if (0L != sLowMemoryThresold) {
            return sLowMemoryThresold
        }
        getTotalMemory(context)
        return sLowMemoryThresold
    }

    //in KB
    fun getMemoryClass(context: Context): Int {
        if (0 != sMemoryClass) {
            return sMemoryClass * 1024
        }
        getTotalMemory(context)
        return sMemoryClass * 1024
    }

    fun getTotalMemory(context: Context): Long {
        if (0L != sTotalMemory) {
            return sTotalMemory
        }
        val start = System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val memInfo = ActivityManager.MemoryInfo()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.getMemoryInfo(memInfo)
            sTotalMemory = memInfo.totalMem
            sLowMemoryThresold = memInfo.threshold
            val memClass = Runtime.getRuntime().maxMemory()
            sMemoryClass = if (memClass == Long.MAX_VALUE) {
                am.memoryClass //if not set maxMemory, then is not large heap
            } else {
                (memClass / MB).toInt()
            }
            //            int isLargeHeap = (context.getApplicationInfo().flags | ApplicationInfo.FLAG_LARGE_HEAP);
//            if (isLargeHeap > 0) {
//                sMemoryClass = am.getLargeMemoryClass();
//            } else {
//                sMemoryClass = am.getMemoryClass();
//            }
            MatrixLog.i(
                TAG,
                "getTotalMemory cost:" + (System.currentTimeMillis() - start) + ", total_mem:" + sTotalMemory
                        + ", LowMemoryThresold:" + sLowMemoryThresold + ", Memory Class:" + sMemoryClass
            )
            return sTotalMemory
        }
        return 0
    }

    fun isLowMemory(context: Context): Boolean {
        val memInfo = ActivityManager.MemoryInfo()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.getMemoryInfo(memInfo)
        return memInfo.lowMemory
    }

    //return in KB
    fun getAvailMemory(context: Context?): Long {
        val runtime = Runtime.getRuntime()
        return runtime.freeMemory() / 1024 //in KB
    }

    fun getMemFree(context: Context): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val memInfo = ActivityManager.MemoryInfo()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.getMemoryInfo(memInfo)
            memInfo.availMem / 1024
        } else {
            var availMemory = INVALID.toLong()
            var bufferedReader: BufferedReader? = null
            try {
                bufferedReader =
                    BufferedReader(InputStreamReader(FileInputStream(MEMORY_FILE_PATH), "UTF-8"))
                var line = bufferedReader.readLine()
                while (null != line) {
                    val args = line.split("\\s+").toTypedArray()
                    if ("MemAvailable:" == args[0]) {
                        availMemory = args[1].toInt() * 1024L
                        break
                    } else {
                        line = bufferedReader.readLine()
                    }
                }
            } catch (e: Exception) {
                MatrixLog.i(TAG, "[getAvailMemory] error! %s", e.toString())
            } finally {
                try {
                    bufferedReader?.close()
                } catch (e: Exception) {
                    MatrixLog.i(TAG, "close reader %s", e.toString())
                }
            }
            availMemory / 1024
        }
    }

    fun getAppCpuRate(): Double {
        val start = System.currentTimeMillis()
        var cpuTime = 0L
        var appTime = 0L
        var cpuRate = 0.0
        var procStatFile: RandomAccessFile? = null
        var appStatFile: RandomAccessFile? = null
        try {
            procStatFile = RandomAccessFile("/proc/stat", "r")
            val procStatString = procStatFile.readLine()
            val procStats = procStatString.split(" ").toTypedArray()
            cpuTime =
                procStats[2].toLong() + procStats[3].toLong() + procStats[4].toLong() + procStats[5].toLong() + procStats[6].toLong() + procStats[7].toLong() + procStats[8].toLong()
        } catch (e: Exception) {
            MatrixLog.i(TAG, "RandomAccessFile(Process Stat) reader fail, error: %s", e.toString())
        } finally {
            try {
                procStatFile?.close()
            } catch (e: Exception) {
                MatrixLog.i(TAG, "close process reader %s", e.toString())
            }
        }
        try {
            appStatFile = RandomAccessFile("/proc/" + getAppId() + "/stat", "r")
            val appStatString = appStatFile.readLine()
            val appStats = appStatString.split(" ").toTypedArray()
            appTime = appStats[13].toLong() + appStats[14].toLong()
        } catch (e: Exception) {
            MatrixLog.i(TAG, "RandomAccessFile(App Stat) reader fail, error: %s", e.toString())
        } finally {
            try {
                appStatFile?.close()
            } catch (e: Exception) {
                MatrixLog.i(TAG, "close app reader %s", e.toString())
            }
        }
        if (0L != cpuTime) {
            cpuRate = appTime.toDouble() / cpuTime.toDouble() * 100.0
        }
        MatrixLog.i(
            TAG,
            "getAppCpuRate cost:" + (System.currentTimeMillis() - start) + ",rate:" + cpuRate
        )
        return cpuRate
    }

    fun getAppMemory(context: Context): Debug.MemoryInfo? {
        try {
            // 统计进程的内存信息 totalPss
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = activityManager.getProcessMemoryInfo(intArrayOf(getAppId()))
            if (memInfo.isNotEmpty()) {
                return memInfo[0]
            }
        } catch (e: Exception) {
            MatrixLog.i(TAG, "getProcessMemoryInfo fail, error: %s", e.toString())
        }
        return null
    }

    private fun getNumOfCores(): Int {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1
        }
        var cores: Int
        try {
            cores = getCoresFromFile(CPU_FILE_PATH_1)
            if (cores == INVALID) {
                cores = getCoresFromFile(CPU_FILE_PATH_2)
            }
            if (cores == INVALID) {
                cores = getCoresFromCPUFiles(CPU_FILE_PATH_0)
            }
        } catch (e: Exception) {
            cores = INVALID
        }
        if (cores == INVALID) {
            cores = 1
        }
        return cores
    }

    private fun getCoresFromCPUFiles(path: String): Int {
        val list = File(path).listFiles(CPU_FILTER)
        return list?.size ?: 0
    }

    private fun getCoresFromFile(file: String): Int {
        var `is`: InputStream? = null
        return try {
            `is` = FileInputStream(file)
            val buf = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            val fileContents = buf.readLine()
            buf.close()
            if (fileContents == null || !fileContents.matches(Regex("0-[\\d]+$"))) {
                return INVALID
            }
            val num = fileContents.substring(2)
            num.toInt() + 1
        } catch (e: IOException) {
            MatrixLog.i(TAG, "[getCoresFromFile] error! %s", e.toString())
            INVALID
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                MatrixLog.i(TAG, "[getCoresFromFile] error! %s", e.toString())
            }
        }
    }

    private val CPU_FILTER =
        FileFilter { pathname -> Pattern.matches("cpu[0-9]", pathname.name) }

    @JvmStatic
    fun getDalvikHeap(): Long {
        val runtime = Runtime.getRuntime()
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024 //in KB
    }
    @JvmStatic
    fun getNativeHeap(): Long {
        return Debug.getNativeHeapAllocatedSize() / 1024 //in KB
    }
    @JvmStatic
    fun getVmSize(): Long {
        val status = String.format("/proc/%s/status", getAppId())
        try {
            val content = getStringFromFile(status).trim { it <= ' ' }
            val args = content.split("\n").toTypedArray()
            for (str in args) {
                if (str.startsWith("VmSize")) {
                    val p = Pattern.compile("\\d+")
                    val matcher = p.matcher(str)
                    if (matcher.find()) {
                        return matcher.group().toLong()
                    }
                }
            }
            if (args.size > 12) {
                val p = Pattern.compile("\\d+")
                val matcher = p.matcher(args[12])
                if (matcher.find()) {
                    return matcher.group().toLong()
                }
            }
        } catch (e: Exception) {
            return -1
        }
        return -1
    }

    @Throws(Exception::class)
    fun convertStreamToString(`is`: InputStream?): String {
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
        } finally {
            reader?.close()
        }
        return sb.toString()
    }

    @Throws(Exception::class)
    fun getStringFromFile(filePath: String?): String {
        val fl = File(filePath)
        var fin: FileInputStream? = null
        val ret: String
        try {
            fin = FileInputStream(fl)
            ret = convertStreamToString(fin)
        } finally {
            fin?.close()
        }
        return ret
    }

    /**
     * Check if current runtime is 64bit.
     *
     * @return
     * True if current runtime is 64bit abi. Otherwise return false instead.
     */
    @JvmStatic
    fun is64BitRuntime(): Boolean {
        val currRuntimeABI = Build.CPU_ABI
        return ("arm64-v8a".equals(currRuntimeABI, ignoreCase = true)
                || "x86_64".equals(currRuntimeABI, ignoreCase = true)
                || "mips64".equals(currRuntimeABI, ignoreCase = true))
    }

    @JvmStatic
    fun dumpMemory(): LongArray? {
        val memory = LongArray(3)
        memory[0] = getDalvikHeap()
        memory[1] = getNativeHeap()
        memory[2] = getVmSize()
        return memory
    }
}