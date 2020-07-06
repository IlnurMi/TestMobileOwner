package com.ilnur.mobileowner.presentation.presenters.register

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import com.ilnur.mobileowner.data.network.NoConnectivityException
import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.data.utils.PasswordValidator
import ru.itis.kalimullinri.itisowner.domain.interactors.profile.ProfileInteractor
import com.ilnur.mobileowner.domain.models.response.CustomResult
import com.ilnur.mobileowner.interfaces.views.register.RegisterView
import java.util.regex.Pattern

class RegisterPresenter(private val profileInteractor: ProfileInteractor) {
    private var registerView: RegisterView? = null
    private var disposable: Disposable? = null

    fun setView(registerView: RegisterView) {
        this.registerView = registerView
    }

    fun clickRegisterJuridical(
        email: String,
        phone: String,
        password: String,
        rePassword: String,
        name: String,
        address: String,
        bankAccount: String,
        inn: String,
        kpp: String,
        ogrn: String,
        description: String
    ) {

        if (!validForEmptyJuridicalFields(
                email, phone, password, rePassword, name, address,
                bankAccount, inn, kpp, ogrn, description
            )
        ) {
            registerView?.errorEmptyFields()
            return
        }

        if (!isEmailValid(email)) {
            registerView?.errorEmailNotValid()
            return
        }

        if (!isPasswordsEquals(password, rePassword)) {
            registerView?.errorPasswordNotEquals()
            return
        }

        if (!isPasswordValid(password)) {
            registerView?.errorPasswordNotValid(password)
            return
        }

        val prepareJuridicalProfile = profileInteractor.prepareJuridicalProfile(
            email,
            phone,
            password,
            rePassword,
            name,
            address,
            bankAccount,
            inn,
            kpp,
            ogrn,
            description
        )
        disposable = profileInteractor
            .registerNeJuridicalUser(prepareJuridicalProfile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ customResult -> responseJuridicalSuccess(customResult) },
                { error -> responseError(error) })
    }

    fun clickRegisterPhysical(
        email: String,
        phone: String,
        password: String,
        rePassword: String,
        lastName: String,
        firstName: String,
        secondName: String,
        birthday: String,
        address: String,
        passport: String,
        bankAccount: String,
        company: String
    ) {
        if (!validForEmptyFields(
                email, phone, password, rePassword, lastName, firstName,
                secondName, birthday, address, passport, bankAccount, company
            )
        ) {
            registerView?.errorEmptyFields()
            return
        }

        if (!isEmailValid(email)) {
            registerView?.errorEmailNotValid()
            return
        }

        if (!isPasswordsEquals(password, rePassword)) {
            registerView?.errorPasswordNotEquals()
            return
        }

        if (!isPasswordValid(password)) {
            registerView?.errorPasswordNotValid(password)
            return
        }

        registerView?.showHideLoadingView(true)
        registerView?.enableViews(false)

        val preparePhysicalProfile = profileInteractor.preparePhysicalProfile(
            email,
            phone,
            password,
            rePassword,
            lastName,
            firstName,
            secondName,
            birthday,
            address,
            passport,
            bankAccount,
            company
        )
        disposable = profileInteractor
            .registerNewPhysicalUser(preparePhysicalProfile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ customResult -> responseSuccess(customResult) },
                { error -> responseError(error) })
    }

    private fun responseJuridicalSuccess(customResult: CustomResult?) {
        //TODO check fails
    }

    private fun responseSuccess(customResult: CustomResult?) {
        if (customResult?.status?.toInt() == 409) {
            registerView?.userAlreadyExist()
            return
        }
        if (customResult?.status?.toInt() == 400) {
            registerView?.registerError()
            return
        }
        if (customResult?.status?.toInt() == 201 || customResult?.status?.toInt() == 200) {
            registerView?.registerSuccess()
        }
        profileInteractor.saveUserToPreference();
    }

    private fun responseError(error: Throwable?) {
        registerView?.showError()
        registerView?.showHideLoadingView(false)
        registerView?.enableViews(true)
        error?.printStackTrace()
        if (error is HttpException) {
            when (error.code()) {
                ConstantUtils.SERVER_CODE_500 -> registerView?.errorServer()
            }
            return
        }
        if (error is NoConnectivityException) {
            registerView?.errorNoInternetConnection()
            return
        }
    }


    private fun validForEmptyFields(
        email: String,
        phone: String,
        password: String,
        rePassword: String,
        lastName: String,
        firstName: String,
        secondName: String,
        birthday: String,
        address: String,
        passport: String,
        bankAccount: String,
        company: String
    ): Boolean {
        return email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() &&
                rePassword.isNotEmpty() && lastName.isNotEmpty() && firstName.isNotEmpty() &&
                secondName.isNotEmpty() && birthday.isNotEmpty() && address.isNotEmpty() &&
                passport.isNotEmpty() && bankAccount.isNotEmpty() && company.isNotEmpty()
    }

    private fun validForEmptyJuridicalFields(
        email: String,
        phone: String,
        password: String,
        rePassword: String,
        name: String,
        address: String,
        bankAccount: String,
        inn: String,
        kpp: String,
        ogrn: String,
        description: String
    ): Boolean {
        return email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() &&
                rePassword.isNotEmpty() && name.isNotEmpty() && address.isNotEmpty() &&
                bankAccount.isNotEmpty() && inn.isNotEmpty() && kpp.isNotEmpty() &&
                ogrn.isNotEmpty() && description.isNotEmpty()
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

    private fun isPasswordValid(password: String): Boolean =
        PasswordValidator.instance.validate(password)

    private fun isPasswordsEquals(password: String, rePassword: String): Boolean {
        return password.equals(rePassword)
    }

    fun onStop() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable?.dispose()
        }
    }

}