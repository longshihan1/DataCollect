package com.longshihan.collect.control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.longshihan.collect.R
import com.longshihan.collect.http.UploadUtils
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        l_testconnect.setOnClickListener {
            UploadUtils.startLogin()
        }
        l_openupload.setOnClickListener {
            UploadUtils.uploadMsg()
        }
    }
}