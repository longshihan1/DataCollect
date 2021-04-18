package com.longshihan.datacollect1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.longshihan.collect.http.UploadUtils
import com.longshihan.collect.init.Trace
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testConnect.setOnClickListener {
            UploadUtils.startLogin()
            UploadUtils.uploadMsg()
        }
        addMethod.setOnClickListener {
            val tag:String= System.currentTimeMillis().toString()
            Trace.initFirst(MainActivity::class.java.name, "onclick",tag)
            val count = Random(25).nextInt(10)
            for (index in 0..count) {
                val text = TextView(this)
            }
            Trace.initLast(MainActivity::class.java.name, "onclick",tag)
            handler()
        }
    }

    private fun handler() {
        android.os.Trace.beginSection("");
        Thread().run {
        try {
            Log.d("测试","线程")
            Thread.sleep(10000)
        }catch (e:Exception){

        }
        }
        android.os.Trace.endSection();
    }
}