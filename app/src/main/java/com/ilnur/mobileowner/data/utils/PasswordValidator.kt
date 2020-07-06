package com.ilnur.mobileowner.data.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class PasswordValidator private constructor(){

    private object HOLDER{
        val INSTANCE = PasswordValidator()
    }

    companion object {
        val instance: PasswordValidator by lazy { HOLDER.INSTANCE }
    }

    private lateinit var pattern: Pattern
    private var matcher: Matcher? = null

    fun validate(password: String): Boolean {
        pattern = Pattern.compile(ConstantUtils.PASSWORD_VALIDATOR_EXPRESSION)
        matcher = pattern.matcher(password)
        return matcher!!.matches()

    }
}