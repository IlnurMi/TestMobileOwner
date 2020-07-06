package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class DefaultObjectRequest(@SerializedName("value")
                           val id: String,
                           @SerializedName("text")
                           val name: String) : IPojo.JsonObject {
    override fun toString(): String = name
}