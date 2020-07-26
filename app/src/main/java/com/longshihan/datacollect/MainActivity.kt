package com.longshihan.datacollect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.longshihan.collect.http.UploadUtils
import com.longshihan.collect.init.Trace
import kotlinx.android.synthetic.main.activity_main.*
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
            Trace.initFirst(MainActivity::class.java.name, "onclick")
            val count = Random(25).nextInt(10)
            for (index in 0..count) {
                val text = TextView(this)
            }
            Trace.initLast(MainActivity::class.java.name, "onclick")

        }
    }
}