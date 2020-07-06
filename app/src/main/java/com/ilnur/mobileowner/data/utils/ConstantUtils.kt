package com.ilnur.mobileowner.data.utils

class ConstantUtils {
    companion object {
        const val EMAIL_VALIDATOR_EXPRESSION = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        const val PASSWORD_VALIDATOR_EXPRESSION = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!]).{6,20})"
        const val PASSWORD_SYMBOLS_COUNT: Int = 8

        // Permissions code
        const val WRITE_STORAGE_PERMISSION = 100
        const val SEND_SMS_PERMISSION = 200
        const val CALL_PERMISSION = 300

        // Параметры ключи
        const val KEY_PARAM_GRANT_TYPE: String = "grant_type"
        const val KEY_PARAM_USERNAME: String = "username"
        const val KEY_PARAM_PASSWORD: String = "password"
        const val KEY_PARAM_NAME: String = "title"
        const val KEY_PARAM_SURNAME: String = "surname"
        const val KEY_PARAM_COMPANY: String = "company"
        const val KEY_PARAM_PHONE: String = "phone"
        const val KEY_PARAM_EMAIL: String = "email"
        const val KEY_PARAM_ADDREES: String = "Name"

        // Shared Preferences
        const val PREFS_NAME_APP: String = "PREFS_APP"
        const val PREF_TOKEN: String = "TOKEN"
        const val PREF_IS_AUTH: String = "IS_AUTH"
        //const val PREF_NAME: String = "NAME"
        const val PREF_SURNAME: String = "SURNAME"
        const val PREF_PHONE: String = "PHONE"
        //const val PREF_COMPANY: String = "COMPANY"
        const val PREF_EMAIL: String = "EMAIL"
        const val PREF_ADDRESS: String = "ADDRESS"
        const val PREF_PASSWORD: String = "PASSWORD"
        const val PREF_APPLY_NDK: String = "APPLY_NDK"
        const val PREF: String = "wdwa"

        const val PREF_RESULT: String = "RESULT"
        const val PREF_STATUS: String = "STATUS"
        const val PREF_FIRST_NAME: String = "FIRST_NAME"
        const val PREF_SECOND_NAME: String = "SECOND_NAME"
        const val PREF_LAST_NAME: String = "LAST_NAME"
        const val PREF_NAME: String = "NAME"
        const val PREF_FULL_NAME: String = "FULL_NAME"
        const val PREF_BIRTHDAY: String = "BIRTHDAY"
        const val PREF_FACT_ADDRESS: String = "FACT_ADDRESS"
        const val PREF_REGISTRATION_ADDRESS: String = "REGISTRATION_ADDRESS"
        const val PREF_PASSPORT: String = "PASSPORT"
        const val PREF_CHECKING_ACCOUNT: String = "CHECKING_ACCOUNT"
        const val PREF_PHOTO: String = "PHOTO"
        const val PREF_INN: String = "INN"
        const val PREF_KPP: String = "KPP"
        const val PREF_OGRN: String = "OGRN"
        const val PREF_ID: String = "ID"
        const val PREF_COMPANY: String = "COMPANY"
        const val PREF_DESCRIPTION: String = "DESCRIPTION"
        const val PREF_IS_JURIDICAL_PERSON: String = "IS_JURIDICAL_PERSON"
        const val PREF_CREATE_DATE: String = "CREATE_DATE"
        const val PREF_MODIFY_DATE: String = "MODIFY_DATE"
        const val PREF_ROLE: String = "ROLE"

        const val DRAWER_MAP: Int = 1
        const val DRAWER_MESSAGE: Int = 2
        const val DRAWER_SEARCH_CARS: Int = 5
        const val DRAWER_PROFILE: Int = 7
        const val DRAWER_SETTINGS: Int = 8
        const val DRAWER_MANAGERS: Int = 11
        const val DRAWER_REPORT: Int = 13

        const val TASK_EDIT: Int = 2
        const val TASK_NEW: Int = 1

        // Map types
        const val MAP_TYPE_NORMAL:String = "normal"
        const val MAP_TYPE_SATELLITE:String = "satellite"
        const val MAP_TYPE_TERRAIN:String = "terrain"
        const val MAP_TYPE_HYBRID:String = "hybrid"

        //check task types
         var TASKTYPE: String = ""

        //Tabs

        //Статусы сервера
        const val STATUS_CODE_FORBIDDEN = 403
        const val STATUS_CODE_CONFLICT = 409
        const val STATUS_CODE_BAD_GATEWAY = 502
        const val SERVER_CODE_400: Int = 400
        const val SERVER_CODE_500: Int = 500
        const val SERVER_URL: String = ""

        //Roles
        const val ROLE_OWNER: Int = 50

        const val IS_JURIDICAL_PERSON: Int = 1
        const val IS_NOT_JURIDICAL_PERSON: Boolean = false

        const val SELF_REGISTRATION: Boolean = true

        const val CONFIRMATION: Boolean = true

        const val FIREBASE_TOKEN: String = "FIREBASE_TOKEN"

        // константы для выбора периода (дата начала и дата конца)
        const val DATE_BEGIN: String = "00:00"
        const val DATE_END: String = "23:59"
    }
}