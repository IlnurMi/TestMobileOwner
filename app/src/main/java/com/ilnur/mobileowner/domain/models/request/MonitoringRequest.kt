package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.response.MonitoringInfo

class MonitoringRequest {
    @SerializedName("success")
    lateinit var status: String
    @SerializedName("result")
    lateinit var result: String
    @SerializedName("objects")
    @Expose
    var objects: MonitoringInfo? = null
}