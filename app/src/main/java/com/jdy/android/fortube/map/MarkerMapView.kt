package com.jdy.android.fortube.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MarkerMapView(context: Context, attrs: AttributeSet): MapView(context, attrs), MarkerMapViewEventListener {
    private lateinit var mMapViewModel: MapViewModel

    fun setMapViewModel(mapViewModel: MapViewModel) {
        mMapViewModel = mapViewModel
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initializeMap()
    }

    private fun initializeMap() {
        setMapTilePersistentCacheEnabled(true)
        setMapViewEventListener(this)
    }

    fun startLocationTracking() {
//        setShowCurrentLocationMarker(true)
        currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    private fun addMarker(point: MapPoint) {
        addPOIItem(MapPOIItem().apply {
            itemName = "default"
            tag = 0
            mapPoint = point
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        })
    }

    override fun onMapViewMoveFinished(mapView: MapView?, point: MapPoint?) {
        Log.d("MarkerMapView", "onMapViewMoveFinished [$point]")
    }

    override fun onMapViewCenterPointMoved(mapView: MapView?, point: MapPoint?) {
        Log.d("MarkerMapView", "onMapViewCenterPointMoved [$point]")
    }

    override fun onPOIItemSelected(mapView: MapView?, item: MapPOIItem?) {
        Log.i("MarkerMapView", "onPOIItemSelected [$item]")
    }
}