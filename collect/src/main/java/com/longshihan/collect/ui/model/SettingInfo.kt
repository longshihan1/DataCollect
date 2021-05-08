package com.longshihan.collect.ui.model

import com.longshihan.collect.init.TraceManager
import com.longshihan.collect.utils.Constants
import org.json.JSONObject


data class SettingInfo(
    val bthreadStack: Boolean = false,
    val bjvmti: Boolean = false,
    val bIO: Boolean = true,
    val ipAdress: String = "",
    val anrTime: Long = 5000L,
    val evilTime: Long = 5000L
) {
    fun saveJson() {
        val jsonObject = JSONObject()
        jsonObject.put("bthreadStack", bthreadStack)
        jsonObject.put("bjvmti", bjvmti)
        jsonObject.put("bIO", bIO)
        jsonObject.put("ipAdress", ipAdress)
        jsonObject.put("anrTime", anrTime)
        jsonObject.put("evilTime", evilTime)
        TraceManager.sharePreferenceUtils.setString(Constants.SETTINGINFO, jsonObject.toString())
    }

    companion object {
        fun getInfo(): SettingInfo {
            val json = TraceManager.sharePreferenceUtils.getString(Constants.SETTINGINFO)
            return if (json.isNullOrEmpty()) {
                SettingInfo()
            } else {
                val jsonObject = JSONObject(json)
                SettingInfo(
                    jsonObject.getBoolean("bthreadStack"),
                    jsonObject.getBoolean("bjvmti"),
                    jsonObject.getBoolean("bIO"),
                    jsonObject.getString("ipAdress"),
                    jsonObject.getLong("anrTime"),
                    jsonObject.getLong("evilTime")
                )
            }
        }
    }
}