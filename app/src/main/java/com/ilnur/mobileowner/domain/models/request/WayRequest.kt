package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class WayRequest(@SerializedName("way")
                 val way: List<WayObject>): IPojo.JsonArray {
}