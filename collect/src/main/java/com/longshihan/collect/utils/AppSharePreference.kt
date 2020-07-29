package com.longshihan.collect.utils

import android.content.Context
import android.content.SharedPreferences
import com.longshihan.collect.init.Trace
import com.longshihan.collect.init.TraceManager

object AppSharePreference {
    val FILE_NAME="DataCollect"
    var context:Context?=null
    var sp:SharedPreferences?=null

    fun init(mContext: Context){
        context=mContext
        sp= mContext.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
    }
    fun put(key:String,any: Any){
        val editor= sp?.edit()
        when (any) {
            is String -> {}
            is Number -> {}
            is Boolean->{}
            is Float->{}
            is Long->{}
            else->{

            }
        }
    }
}