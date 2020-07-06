package com.ilnur.mobileowner.domain.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomResult {
    @SerializedName("result")
    @Expose
    var result: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("objects")
    @Expose
    var objects: String? = null
}