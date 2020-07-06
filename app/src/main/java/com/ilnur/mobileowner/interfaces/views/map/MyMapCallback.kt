package com.ilnur.mobileowner.interfaces.views.map

import com.google.android.gms.maps.model.Marker

interface MyMapCallback {
    fun showRoute(dateBegin: String, dateEnd: String, name: String)
    fun copyLtng(lat: String)
    fun changeColors(marker: Marker)
}