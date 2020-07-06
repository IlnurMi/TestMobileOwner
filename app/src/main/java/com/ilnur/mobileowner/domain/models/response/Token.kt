package com.ilnur.mobileowner.domain.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {
    @SerializedName("auth_token")
    @Expose
    var accessToken: String? = null
    @SerializedName("user_name")
    @Expose
    var userName: String? = null
    @SerializedName("is_user_admin")
    @Expose
    var isUserAdmin: Boolean? = null
}