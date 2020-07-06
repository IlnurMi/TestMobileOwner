package com.ilnur.mobileowner.data.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class HelperUtil private constructor() {
    private object HOLDER {
        val INSTANCE = HelperUtil()
    }

    companion object {
        val instance: HelperUtil by lazy { HOLDER.INSTANCE }
    }

    fun hideKeyboard(context: Context, view: View) {
        view.let {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}