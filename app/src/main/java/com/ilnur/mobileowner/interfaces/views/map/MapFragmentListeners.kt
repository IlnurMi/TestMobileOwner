package com.ilnur.mobileowner.interfaces.views.map

import com.google.android.gms.maps.model.LatLng

interface MapFragmentListeners {
    fun moveCamera(latLng: LatLng)
}