package com.ilnur.mobileowner.presentation.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap


class MarkerClusterItem(carOnMap: CarOnMap): ClusterItem {
    private var lat = carOnMap.lat
    private var long =  carOnMap.lon
    private var latLng = LatLng(lat!!.toDouble(),long!!.toDouble())
    private var title = carOnMap.name.toString()
    private var carOnMap = carOnMap

    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return ""
    }

    fun getCar(): CarOnMap {
        return carOnMap
    }

}