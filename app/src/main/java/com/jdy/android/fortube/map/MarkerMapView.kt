package com.jdy.android.fortube.map

import android.content.Context
import android.util.AttributeSet
import net.daum.mf.map.api.MapView

class MarkerMapView(context: Context, attrs: AttributeSet): MapView(context, attrs) {
    private lateinit var mMapViewModel: MapViewModel

    fun setMapViewModel(mapViewModel: MapViewModel) {
        mMapViewModel = mapViewModel
    }

}