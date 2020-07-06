package com.ilnur.mobileowner.presentation.ui.extensions

fun String.changeDateTime(time: String): String{
    return if (length == 16) {
        this
    } else {
        "${take(10)} $time"
    }
}