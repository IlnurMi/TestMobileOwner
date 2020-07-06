package com.ilnur.mobileowner

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.ilnur.mobileowner.data.preferences.PreferenceRepository

class App: Application() {

    var preferenceRepository: PreferenceRepository? = null

    override fun onCreate() {
        super.onCreate()

        preferencesInit()
        //initFabric()
    }

    private fun initFabric() {
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true) // Enables Crashlytics debugger
            .build()
        Fabric.with(fabric)
        Crashlytics.setUserIdentifier("user123456789")
        Crashlytics.setString("field", "foo" /* string value */)
        Crashlytics.log(Log.DEBUG, "tag", "message")
    }

    private fun preferencesInit() {
        preferenceRepository = PreferenceRepository.INSTANCE
        preferenceRepository!!.init(applicationContext)
    }
}