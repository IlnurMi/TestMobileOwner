package com.ilnur.mobileowner.interfaces.views.register

import com.ilnur.mobileowner.interfaces.ErrorsInterface

interface RegisterView : ErrorsInterface {
    fun clickListener()
    fun registerSuccess()
    fun userAlreadyExist()
    fun errorEmptyFields()
    fun errorEmailNotValid()
    fun errorPasswordNotEquals()
    fun errorPasswordNotValid(password: String)
    fun showHideLoadingView(b: Boolean)
    fun enableViews(b: Boolean)
    fun initToolbar()
    fun initViews()
    fun registerError()
    fun showError()
}