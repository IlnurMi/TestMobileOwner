package com.ilnur.mobileowner.domain.models.request


import androidx.room.Ignore

data class Place(var Lat: String = "",
                 var Lon: String = "",
                 var Name: String = ""){
    @Ignore
    constructor() : this(Lat = "", Lon = "", Name = "")
}
