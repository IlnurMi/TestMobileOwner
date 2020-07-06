package com.ilnur.mobileowner.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.response.CustomResult

class MainResponceArray<T : IPojo.JsonArray> {
    @SerializedName("result")
    lateinit var result: String
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("objects")
    @Expose
    var list: List<T> = emptyList()

    var resultObject: CustomResult? = null
}