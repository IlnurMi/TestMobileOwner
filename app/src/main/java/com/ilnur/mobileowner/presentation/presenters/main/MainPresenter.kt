package com.ilnur.mobileowner.presentation.presenters.main

import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.interfaces.views.main.MainView
import com.ilnur.mobileowner.presentation.ui.map.MyMapFragment
import com.ilnur.mobileowner.presentation.ui.report.ReportPagerFragment
import com.ilnur.mobileowner.presentation.ui.settings.SettingFragment

class MainPresenter {
    private var mainView: MainView? = null

    fun setView(view: MainView) {
        mainView = view
    }

    fun drawerItemClick(drawerItem: Int) {
        when(drawerItem){
            ConstantUtils.DRAWER_REPORT -> mainView?.replaceFragment(ReportPagerFragment.getInstance())
            ConstantUtils.DRAWER_SETTINGS -> mainView?.replaceFragment(SettingFragment.getInstance())
            ConstantUtils.DRAWER_MAP -> mainView?.replaceFragment(MyMapFragment.getInstance())
        }
    }
}