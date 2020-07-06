package com.ilnur.mobileowner.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.domain.models.request.Objects
import com.ilnur.mobileowner.domain.models.request.Profile

class PreferenceRepository private constructor() {

    private lateinit var context: Context
    private lateinit var preferences: SharedPreferences

    private object HOLDER {
        val INSTANCE = PreferenceRepository()
    }

    companion object {
        val INSTANCE: PreferenceRepository by lazy { HOLDER.INSTANCE }
    }

    fun init(context: Context) {
        this.context = context
        preferences = context.getSharedPreferences(ConstantUtils.PREFS_NAME_APP, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String){
        var editor = preferences.edit()
        editor.putString(ConstantUtils.PREF_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String = "Bearer ${preferences.getString(ConstantUtils.PREF_TOKEN, "")}"

    fun setAuthorized(isAuth: Boolean) {
        var editor = preferences.edit()
        editor.putBoolean(ConstantUtils.PREF_IS_AUTH, isAuth)
        editor.apply()
    }

    fun isAuthorised(): Boolean = preferences.getBoolean(ConstantUtils.PREF_IS_AUTH, false)

    fun saveEmailPassword(email: String, password: String){
        var editor = preferences.edit()
        editor.putString(ConstantUtils.KEY_PARAM_EMAIL, email)
        editor.putString(ConstantUtils.KEY_PARAM_PASSWORD, password)
        editor.apply()
    }

    fun getEmail(): String{
        return preferences.getString(ConstantUtils.KEY_PARAM_EMAIL,"").toString()
    }

    fun getPassword(): String{
        return preferences.getString(ConstantUtils.KEY_PARAM_PASSWORD,"").toString()
    }

    fun getUserId(): String{
        return preferences.getString(ConstantUtils.PREF_ID, "").toString()
    }

    fun getPhoto(): String{
        return preferences.getString(ConstantUtils.PREF_PHOTO, "").toString()
    }

    fun saveFirebaseToken(token: String){
        var editor = preferences.edit()
        editor.putString(ConstantUtils.FIREBASE_TOKEN, token)
        editor.apply()
    }

    fun getFirebaseToken(): String{
        return preferences.getString(ConstantUtils.FIREBASE_TOKEN, "").toString()
    }

    fun saveRegisterUser(profile: Profile) {
        var editor = preferences.edit()
        editor.putString(ConstantUtils.PREF_FIRST_NAME, profile.firstName)
        editor.putString(ConstantUtils.PREF_SECOND_NAME, profile.secondName)
        editor.putString(ConstantUtils.PREF_LAST_NAME, profile.lastName)
        editor.putString(ConstantUtils.PREF_NAME, profile.name)
        editor.putString(ConstantUtils.PREF_FULL_NAME, profile.fullName)
        editor.putString(ConstantUtils.PREF_BIRTHDAY, profile.birthday)
        editor.putString(ConstantUtils.PREF_FACT_ADDRESS, profile.factAddress)
        editor.putString(ConstantUtils.PREF_REGISTRATION_ADDRESS, profile.registrationAddress)
        editor.putString(ConstantUtils.PREF_PASSPORT, profile.passport)
        editor.putString(ConstantUtils.PREF_CHECKING_ACCOUNT, profile.checkingAccount)
        editor.putString(ConstantUtils.PREF_PHOTO, profile.photo)
        editor.putString(ConstantUtils.PREF_INN, profile.inn)
        editor.putString(ConstantUtils.PREF_KPP, profile.kpp)
        editor.putString(ConstantUtils.PREF_OGRN, profile.ogrn)
        editor.putString(ConstantUtils.PREF_ID, profile.id)
        editor.putString(ConstantUtils.PREF_COMPANY, profile.company)
        editor.putString(ConstantUtils.PREF_DESCRIPTION, profile.description)
        editor.putString(ConstantUtils.PREF_IS_JURIDICAL_PERSON, profile.isJuridicalPerson)
        editor.putString(ConstantUtils.PREF_CREATE_DATE, profile.createDate)
        editor.putString(ConstantUtils.PREF_MODIFY_DATE, profile.modifyDate)
        editor.putString(ConstantUtils.PREF_ROLE, profile.role)
        editor.apply()
    }

    fun getRegisterUser(): Profile {
        val result = preferences.getString(ConstantUtils.PREF_RESULT,"")
        val status = preferences.getString(ConstantUtils.PREF_STATUS,"")
        val objects = Objects(
            ConstantUtils.PREF_FIRST_NAME,
            ConstantUtils.PREF_SECOND_NAME,
            ConstantUtils.PREF_LAST_NAME,
            ConstantUtils.PREF_NAME,
            ConstantUtils.PREF_FULL_NAME,
            ConstantUtils.PREF_BIRTHDAY,
                              ConstantUtils.PREF_FACT_ADDRESS,
            ConstantUtils.PREF_REGISTRATION_ADDRESS,
            ConstantUtils.PREF_PASSPORT,
            ConstantUtils.PREF_CHECKING_ACCOUNT,
            ConstantUtils.PREF_PHOTO,
                              ConstantUtils.PREF_INN,
            ConstantUtils.PREF_KPP,
            ConstantUtils.PREF_OGRN,
            ConstantUtils.PREF_ID,
            ConstantUtils.PREF_COMPANY,
            ConstantUtils.PREF_DESCRIPTION,
            ConstantUtils.PREF_IS_JURIDICAL_PERSON,
                              ConstantUtils.PREF_CREATE_DATE,
            ConstantUtils.PREF_MODIFY_DATE,
            ConstantUtils.PREF_ROLE)
        val firstName = preferences.getString(ConstantUtils.PREF_FIRST_NAME,"")
        val secondName = preferences.getString(ConstantUtils.PREF_SECOND_NAME, "")
        val lastName = preferences.getString(ConstantUtils.PREF_LAST_NAME, "")
        val name = preferences.getString(ConstantUtils.PREF_NAME, "")
        val fullName = preferences.getString(ConstantUtils.PREF_FULL_NAME, "")
        val birthday = preferences.getString(ConstantUtils.PREF_BIRTHDAY,"")
        val factAddress = preferences.getString(ConstantUtils.PREF_FACT_ADDRESS, "")
        val registrationAddress = preferences.getString(ConstantUtils.PREF_REGISTRATION_ADDRESS, "")
        val passport = preferences.getString(ConstantUtils.PREF_PASSPORT, "")
        val checkingAccount = preferences.getString(ConstantUtils.PREF_CHECKING_ACCOUNT, "")
        val photo = preferences.getString(ConstantUtils.PREF_PHOTO,"")
        val inn = preferences.getString(ConstantUtils.PREF_INN, "")
        val kpp = preferences.getString(ConstantUtils.PREF_KPP,"")
        val ogrn = preferences.getString(ConstantUtils.PREF_OGRN,"")
        val id = preferences.getString(ConstantUtils.PREF_ID,"")
        val company = preferences.getString(ConstantUtils.PREF_COMPANY,"")
        val description = preferences.getString(ConstantUtils.PREF_DESCRIPTION,"")
        val isJuridicalPerson = preferences.getString(ConstantUtils.PREF_IS_JURIDICAL_PERSON,"")
        val createDate = preferences.getString(ConstantUtils.PREF_CREATE_DATE,"")
        val modifyDate = preferences.getString(ConstantUtils.PREF_MODIFY_DATE,"")
        val role = preferences.getString(ConstantUtils.PREF_ROLE,"")
        return Profile(
            firstName.toString(),
            secondName.toString(),
            lastName.toString(),
            name.toString(),
            fullName.toString(),
            birthday.toString(),
            factAddress.toString(),
            registrationAddress.toString(),
            passport.toString(),
            checkingAccount.toString(),
            photo.toString(),
            inn.toString(),
            kpp.toString(),
            ogrn.toString(),
            id.toString(),
            company.toString(), description.toString(),
            isJuridicalPerson.toString(), createDate.toString(), modifyDate.toString(), role.toString()
        )
    }

    fun getPhoneNumber(): String = preferences.getString(ConstantUtils.PREF_PHONE, "").toString()

    fun getRegisterFIO(): String = "${preferences.getString(ConstantUtils.PREF_LAST_NAME, "")} ${preferences.getString(
        ConstantUtils.PREF_FIRST_NAME, "")} ${preferences.getString(ConstantUtils.PREF_SECOND_NAME, "")}"

    fun getOrganization(): String = "${preferences.getString(ConstantUtils.PREF_COMPANY, "")}"
}