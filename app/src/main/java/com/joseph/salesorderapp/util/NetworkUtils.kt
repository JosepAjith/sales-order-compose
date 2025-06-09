package com.joseph.salesorderapp.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE

import android.net.ConnectivityManager

fun Context.isNetworkAvailable():Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}
