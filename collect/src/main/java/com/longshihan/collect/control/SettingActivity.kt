package com.longshihan.collect.control

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.longshihan.collect.R
import com.longshihan.collect.http.UploadUtils
import com.longshihan.collect.init.Config
import kotlinx.android.synthetic.main.activity_setting.*
//需要在写一个sp
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
        l_savehost.setOnClickListener {
            Config.HOST=edit_host.text.toString()
        }
    }
}