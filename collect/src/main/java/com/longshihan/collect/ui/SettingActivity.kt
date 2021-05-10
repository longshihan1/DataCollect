package com.longshihan.collect.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.longshihan.collect.R
import com.longshihan.collect.apm.anr.AnrTrace.evilThresholdMs
import com.longshihan.collect.apm.io.IOPlugin
import com.longshihan.collect.http.UploadUtils
import com.longshihan.collect.init.Config
import com.longshihan.collect.init.TraceManager
import com.longshihan.collect.utils.Constants.DEFAULT_ANR
import com.longshihan.collect.utils.data.SharePreferenceUtils
import kotlinx.android.synthetic.main.activity_setting.*

//需要在写一个sp
class SettingActivity : AppCompatActivity() {
    val settingInfo = TraceManager.settingInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        edit_host.setText(settingInfo.ipAdress)
        l_testconnect.setOnClickListener {
            UploadUtils.startLogin()
        }
        l_openupload.setOnClickListener {
            UploadUtils.uploadMsg()
        }
        l_savehost.setOnClickListener {
            Config.HOST = edit_host.text.toString()
            settingInfo.ipAdress = Config.HOST
            settingInfo.saveJson()
        }
        ltransform_io.isChecked = settingInfo.bIO
        ltransform_io.setOnCheckedChangeListener { buttonView, isChecked ->
            settingInfo.bIO = isChecked
            settingInfo.saveJson()
        }

        ltransform_thread.isChecked = settingInfo.bthreadStack
        ltransform_thread.setOnCheckedChangeListener { buttonView, isChecked ->
            settingInfo.bthreadStack = isChecked
            settingInfo.saveJson()
        }

        ltransform_jvmti.isChecked = settingInfo.bjvmti
        ltransform_jvmti.setOnCheckedChangeListener { buttonView, isChecked ->
            settingInfo.bjvmti = isChecked
            settingInfo.saveJson()
        }
        ltransform_anrtv.setOnClickListener {
            val anrTime = ltransform_anredit.text.toString().toLong()
            settingInfo.anrTime = anrTime
            settingInfo.saveJson()
        }
        ltransform_eviltv.setOnClickListener {
            val evilTime = ltransform_eviledit.text.toString().toLong()
            settingInfo.evilTime = evilTime
            settingInfo.saveJson()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (settingInfo.bIO && !IOPlugin.getState()) {
            IOPlugin.start()
        } else if (!settingInfo.bIO && IOPlugin.getState()) {
            IOPlugin.stop()
        }
        DEFAULT_ANR = settingInfo.anrTime
        evilThresholdMs = settingInfo.evilTime

    }

}