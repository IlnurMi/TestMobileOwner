package com.ilnur.mobileowner.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainResponceObject<T : IPojo.JsonObject> {
    @SerializedName("result")
    val result:String = ""
    @SerializedName("status")
    val status:String = ""
    @SerializedName("objects")
    @Expose
    lateinit var obj: T

    //var resultObject: CustomResult? = null
}