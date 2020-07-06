package com.ilnur.mobileowner.presentation.presenters.extensions

import retrofit2.HttpException
import com.ilnur.mobileowner.data.network.NoConnectivityException
import com.ilnur.mobileowner.data.utils.ConstantUtils
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.returnMessage(): String {
    return when(this) {
        is HttpException -> {
            when(this.code()) {
                ConstantUtils.SERVER_CODE_500 -> "Ошибка сервера: 500."
                else -> "Ошибка соединения с сервером:${this.code()}."
            }
        }
        is NoConnectivityException -> {
            "Отсутствует соединение с Интернетом."
        }
        is SocketTimeoutException -> {
            "Превышено время ожидания запроса."
        }
        is UnknownHostException -> {
            "Не удалось подключиться к серверу."
        }
        else -> {
            "Неизвестная ошибка соединения."
        }
    }
}