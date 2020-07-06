package com.ilnur.mobileowner.interfaces.views.login

import com.ilnur.mobileowner.domain.models.response.Token


interface LoginView {
    fun clickListeners()
    fun showLoadingProgressBar(show : Boolean)
    fun showLoginOrPasswordErrorOnServer()
    fun showFieldsIsEmpty()
    fun loginSuccess(token: Token?)
    fun loginSuccessSaveToken(token: Token?)
    fun showEmailNotValid()
    fun showUserNotRegisteredOnServer()
    fun goToRegister(email: String)
    fun errorNoInternetConnection()
    fun register(juridic: Boolean)
    fun loginGetToken()
    fun saveEmailPassword(email: String, password: String)
    fun checkAuthorization()
    fun startBaseActivity()
    fun showToast(message: String)
    fun hideSoftKeyboard()
}