package com.ilnur.mobileowner.domain.models.response

import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

data class DefaultRequest(@SerializedName("value")
                          val id: String,
                          @SerializedName("text")
                          val name: String) : IPojo.JsonArray {
    override fun toString(): String = name
}