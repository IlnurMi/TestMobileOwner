package com.ilnur.mobileowner.interfaces.views.main

import android.view.View
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.DrawerFragmentInterface
import com.ilnur.mobileowner.interfaces.views.ReplaceFragmentInterface

interface MainView {
    fun replaceFragment(newInstance: DrawerFragmentInterface)
    fun initToolbar()
    fun refreshFragment(newInstance: DrawerFragmentInterface)
    fun refreshActivity()
    fun onItemSelected(car: CarOnMap)
    fun showCarOnMap(car: CarOnMap)
    fun joinUserOnline(userId: String)
    fun sendMessage(message: String,
                    recipientId: String,
                    senderId: String, date: String)
    fun hideKeyboard()
    fun clickListeners()
    fun addFragment(newInstance: DrawerFragmentInterface)
    fun enableViews(enable: Boolean)
    fun replaceFragmentWithArrow(fragment: ReplaceFragmentInterface)
    fun showDialogKeyboard(view: View)
}
