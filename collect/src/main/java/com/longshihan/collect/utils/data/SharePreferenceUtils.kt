package com.longshihan.collect.utils.data

import android.content.Context
import android.content.SharedPreferences


/**
 * SharePreference基类
 * 这里只写了对最常用的三种基本数据类型的操作。
 */
open class SharePreferenceUtils(context: Context) {
    private val context: Context = context
    private val sp: SharedPreferences
    private val editor: SharedPreferences.Editor? = null
    private val FILE_NAME = "ltransform"


    fun setString(key: String?, value: String?) {
        sp.edit().putString(key, value).apply()
    }

     fun getString(key: String?): String? {
        return sp.getString(key, null)
    }

     fun setBoolean(key: String?, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

     fun getBoolean(key: String?): Boolean {
        return sp.getBoolean(key, false)
    }

     fun setInt(key: String?, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

     fun getInt(key: String?): Int {
        return sp.getInt(key, 0)
    }

    init {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }
}