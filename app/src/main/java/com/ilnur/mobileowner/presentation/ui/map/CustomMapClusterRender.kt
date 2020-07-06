package com.ilnur.mobileowner.presentation.ui.map

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.ilnur.mobileowner.R
import com.ilnur.mobileowner.presentation.ui.main.MainActivity


class CustomMapClusterRender(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<MarkerClusterItem>?
) : DefaultClusterRenderer<MarkerClusterItem>(context, map, clusterManager) {
    var context = context
    override fun onBeforeClusterItemRendered(
        item: MarkerClusterItem?,
        markerOptions: MarkerOptions?
    ) {
        val markerIcon = getMarkerIcon(root = (context as MainActivity).findViewById(R.id.fragment_main) as ViewGroup, text =  item!!.title, isSelected = true)
        markerOptions!!.icon(markerIcon)
    }

    private fun getMarkerIcon(root: ViewGroup, text: String, isSelected: Boolean): BitmapDescriptor {
        val markerView = CustomMarkerView(root, text, isSelected)
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0,0, markerView.measuredWidth, markerView.measuredHeight)
        markerView.isDrawingCacheEnabled = true
        markerView.invalidate()
        markerView.buildDrawingCache(false)
        return BitmapDescriptorFactory.fromBitmap(markerView.drawingCache)
    }

}