package com.ilnur.mobileowner.presentation.presenters.login

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import com.ilnur.mobileowner.data.network.NoConnectivityException
import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.Profile
import com.ilnur.mobileowner.domain.models.response.Token
import com.ilnur.mobileowner.interfaces.views.login.LoginView
import java.util.regex.Pattern
import io.reactivex.schedulers.Schedulers
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.domain.models.response.CustomResult
import java.net.UnknownHostException

class LoginPresenter(private val profileInteractor: ProfileInteractor) {
    private lateinit var preferenceRepository: PreferenceRepository
    private var loginView: LoginView? = null

    fun setView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun clickAuthorizeButton(email: String, password: String) {

        if (!validate(email, password)) {
            loginView?.showFieldsIsEmpty()
            return
        }

        if (!isEmailValid(email)) {
            loginView?.showEmailNotValid()
            return
        }

        loginView?.apply {
            showLoadingProgressBar(true)
            hideSoftKeyboard()
        }
        profileInteractor?.doAuthorize(email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> responseSuccess(response, email, password) },
                { error -> responseError(error) })
    }

    fun getToken() {
        preferenceRepository = PreferenceRepository.INSTANCE
        profileInteractor
            .doAuthorize(preferenceRepository.getEmail(), preferenceRepository.getPassword())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response -> getTokenResponseSuccess(response) },
                { error -> getTokenResponseError(error) })
    }

    fun pushFirebaseToken(imei: String, token: String, ip: String) {
        var firebaseModel = profileInteractor.getFirebaseModel(imei, token, ip)
        profileInteractor
            .pushFirebaseToken(firebaseModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                pushTokenSuccess(result)
            }, { error ->
                pushTokenError(error)
            })
    }

    private fun pushTokenSuccess(result: CustomResult) {
    }

    private fun pushTokenError(error: Throwable?) {
    }

    private fun getTokenResponseSuccess(response: Token?) {
        loginView?.loginSuccessSaveToken(response)
    }

    private fun getTokenResponseError(error: Throwable?) {
        error?.let { loginView?.showToast("Не удалось подключиться к серверу.") }
    }

    private fun responseError(error: Throwable?) {
        loginView?.showLoadingProgressBar(false)
        when (error) {
            is NoConnectivityException -> loginView?.errorNoInternetConnection()
            is HttpException -> {
                when (error.code()) {
                    ConstantUtils.SERVER_CODE_400 -> loginView!!.showLoginOrPasswordErrorOnServer()
                    ConstantUtils.SERVER_CODE_500 -> loginView!!.showUserNotRegisteredOnServer()
                    else -> loginView?.showToast("Неизвестная ошибка соединения.")
                }
            }
            is UnknownHostException -> loginView?.showToast("Не удалось подключиться к серверу.")
            else -> loginView?.showToast("Неизвестная ошибка соединения.")
        }
    }

    private fun validate(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern =
            Pattern.compile(ConstantUtils.EMAIL_VALIDATOR_EXPRESSION, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            return true
        }
        return false
    }

    private fun responseSuccess(token: Token?, email: String, password: String) {
        val loginSuccessSaveToken = loginView?.loginSuccessSaveToken(token)
        loginView?.saveEmailPassword(email, password)


        profileInteractor
            .getProfile()
            .subscribe(object : SingleObserver<MainResponceObject<Profile>> {
                override fun onSuccess(t: MainResponceObject<Profile>) {
                    profileInteractor.saveUserToPreference(t.obj)
                    loginView?.loginSuccess(token)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }
            })
    }

    fun clickRegisterButton(email: String) {
        loginView?.goToRegister(email)
    }

    fun clickRegister(juridical: Boolean) {
        loginView?.register(juridical)
    }
}