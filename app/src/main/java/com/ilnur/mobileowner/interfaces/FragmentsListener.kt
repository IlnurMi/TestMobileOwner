package com.ilnur.mobileowner.interfaces

import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.views.ReplaceFragmentInterface

interface FragmentsListener {
    fun setActionBarTitle(title: String)
    fun showToast(text: String)
    fun showSearchView(show: Boolean)
    fun replaceDeepFragment(fragment: ReplaceFragmentInterface)
    fun replaceBaseFragment(fragment: DrawerFragmentInterface)
    fun itemSelect(carOnMap: CarOnMap)
    fun saveTextToClipBoard(text: String)
    fun showCloseDialog()
    fun createNotification(fileName: String)
    fun selectedUserChat(id: String)
    fun showArrow()
    fun showHome()
}