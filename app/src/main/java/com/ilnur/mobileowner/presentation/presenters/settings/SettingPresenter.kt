package com.ilnur.mobileowner.presentation.presenters.settings

import com.ilnur.mobileowner.interfaces.views.settings.SettingsContract

class SettingPresenter : SettingsContract.Presenter {

    private lateinit var view: SettingsContract.View
    private val funcNotResolved: String = "Данный функционал на этапе разработки."

    override fun setView(view: SettingsContract.View) {
        this.view = view
    }

    override fun privateItemClicked() {
        view.showToast(funcNotResolved)
    }

    override fun designItemClicked() {
        view.showToast(funcNotResolved)
    }

    override fun soundItemClicked() {
        view.showToast(funcNotResolved)
    }

    override fun languageItemClicked() {
        view.showToast(funcNotResolved)
    }

    override fun helpItemClicked() {
        view.showToast(funcNotResolved)
    }

    override fun exitItemClicked() {
        view.showExitDialog()
    }
}