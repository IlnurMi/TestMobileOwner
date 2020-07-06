package com.ilnur.mobileowner.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!NetworkUtil().isOnline(context)){
            throw  NoConnectivityException("Отсутствует соединение с интернетом")
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}