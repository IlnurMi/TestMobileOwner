package com.ilnur.mobileowner.data.utils

import android.app.Activity
import android.content.Intent
import com.ilnur.mobileowner.data.preferences.PreferenceRepository

class NavigationUtil private constructor(){

    private object HOLDER{
        val INSTANCE = NavigationUtil()
    }

    companion object {
        val instance: NavigationUtil by lazy { HOLDER.INSTANCE }
    }

    fun<T> invokeActivityWithData(activity: Activity, tClass: Class<T>, email: String, shouldFinish: Boolean ) {
        val intent = Intent(activity, tClass)
        intent.putExtra("email", email)
        activity.startActivity(intent)
        if(shouldFinish){
            activity.finish()
        }
    }

    fun<T> invokeActivityRegister(activity: Activity, tClass: Class<T>, juridic: Boolean, shouldFinish: Boolean ) {
        val intent = Intent(activity, tClass)
       intent.putExtra("juridic", juridic)
        activity.startActivity(intent)
        if(shouldFinish){
            activity.finish()
        }
    }

    fun<T> invokeActivityResult(activity: Activity, tClass: Class<T>, intParam: Int, requestCode: Int, shouldFinish: Boolean){
        val intent = Intent(activity, tClass)
        intent.putExtra("int_type", intParam)
        activity.startActivityForResult(intent,requestCode)
        if(shouldFinish){
            activity.finish()
        }
    }

    fun<T> invokeActivityWithoutData(activity: Activity, tClass: Class<T>, shouldFinish: Boolean) {
        val intent = Intent(activity, tClass)
        activity.startActivity(intent)
        if(shouldFinish){
            activity.finish()
        }
    }

    fun<T> invokeActivityWithFlags(activity: Activity, tClass: Class<T>, shouldFinish: Boolean) {
        val intent = Intent(activity, tClass)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP)
        PreferenceRepository.INSTANCE.setAuthorized(false)
        activity.startActivity(intent)
    }
}