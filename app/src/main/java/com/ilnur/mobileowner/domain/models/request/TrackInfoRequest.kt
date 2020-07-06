package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.request.WayRequest

class TrackInfoRequest{
    @SerializedName("status")
    lateinit var status: String
    @SerializedName("result")
    lateinit var result: String
    @SerializedName("objects")
    @Expose
    lateinit var objects: WayRequest
}