package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class ReportCarsList(@SerializedName("id")
                     val id: String,
                     @SerializedName("name")
                     val name: String) : IPojo.JsonArray {
    override fun toString(): String = name
}
