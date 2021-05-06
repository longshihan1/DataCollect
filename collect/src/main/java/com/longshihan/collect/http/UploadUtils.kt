package com.longshihan.collect.http

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.text.method.TextKeyListener.clear
import android.util.Log
import com.google.gson.Gson
import com.longshihan.collect.control.TraceControl
import com.longshihan.collect.http.HttpUtils.saveTraceInfo
import com.longshihan.collect.model.AppDate
import com.longshihan.collect.model.time.SaveTrace
import com.longshihan.collect.traceTime.TraceTime
import com.longshihan.collect.utils.SPUtils
import java.lang.Exception


//先启动一个用户注册接口
//单个接口上传数据，但是数据是整个系统收集，通过tag区分，按时间顺序序列化，本地不做缓存，上传区分用户信息
object UploadUtils {
  private  val mHandlerThread = HandlerThread("LDataCollect")
   const val loginWhat = 10001
    const val msgWhat = 10002
    const val deleyTime = 5000L
    private lateinit var handler: WorkHandler

    fun init() {
        mHandlerThread.start()
        handler= WorkHandler(mHandlerThread.looper,this)
    }

    fun startLogin() {
        handler.sendEmptyMessage(loginWhat)
    }
    @JvmStatic
    fun uploadMsg() {
        handler.sendEmptyMessageDelayed(msgWhat, deleyTime)

    }
    /**
     * 获取一个唯一标识符去登录，并缓存下来，下一次就不登录了
     * 校验网络数据正常
     * 子线程
     */
    fun login() {
        val app = HttpUtils.testConnect()
        Log.d("测试",app.message)
    }

    fun uploadMessage(){
        try {
            val traceList=TraceTime.traceTimeInfos
            val lifecycleList= TraceControl.traceLifecycleInfos
            val fpsList=TraceControl.traceFPSInfos
//            val gson=Gson()
            val appData=AppDate(fpsList,lifecycleList,traceList)
            TraceControl.clearList()
//            Log.d("测试00",appData.dataList.size.toString())
//            val jsonStr=gson.toJson(appData)
//            val app = saveTraceInfo(jsonStr)
            appData.saveSp()
            appData.clear()
//            Log.d("测试",app.message)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    class WorkHandler(looper: Looper,private val uploadUtils: UploadUtils) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                loginWhat -> {
                    uploadUtils.login()
                }//注册
                msgWhat -> {
                    sendEmptyMessageDelayed(msgWhat, deleyTime)
                    uploadUtils.uploadMessage()
                }
            }
        }
    }


}

