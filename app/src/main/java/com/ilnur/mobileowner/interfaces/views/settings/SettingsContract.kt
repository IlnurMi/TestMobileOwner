package com.ilnur.mobileowner.interfaces.views.settings

interface SettingsContract {
    interface View {
        fun clickListeners()
        fun showExitDialog()
        fun showToast(message: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun privateItemClicked()
        fun designItemClicked()
        fun soundItemClicked()
        fun languageItemClicked()
        fun helpItemClicked()
        fun exitItemClicked()
    }
}