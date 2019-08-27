package com.jdy.android.fortube.map

import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

interface MarkerMapViewEventListener: MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {
    override fun onMapViewDoubleTapped(mapView: MapView?, point: MapPoint?) {}
//    override fun onMapViewInitialized(mapView: MapView?) {}
    override fun onMapViewDragStarted(mapView: MapView?, point: MapPoint?) {}
//    override fun onMapViewMoveFinished(mapView: MapView?, point: MapPoint?) {}
//    override fun onMapViewCenterPointMoved(mapView: MapView?, point: MapPoint?) {}
    override fun onMapViewDragEnded(mapView: MapView?, point: MapPoint?) {}
    override fun onMapViewSingleTapped(mapView: MapView?, point: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(mapView: MapView?, zoom: Int) {}
    override fun onMapViewLongPressed(mapView: MapView?, point: MapPoint?) {}
    // ------------------------------------------------------------------------
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, item: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, item: MapPOIItem?, balloonType: MapPOIItem.CalloutBalloonButtonType?) {}
    override fun onDraggablePOIItemMoved(mapView: MapView?, item: MapPOIItem?, point: MapPoint?) {}
//    override fun onPOIItemSelected(mapView: MapView?, item: MapPOIItem?) {}
    // ------------------------------------------------------------------------
    override fun onCurrentLocationUpdateFailed(mapView: MapView?) {}
//    override fun onCurrentLocationUpdate(mapView: MapView?, point: MapPoint?, accuracyInMeters: Float) {}
    override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {}
    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView?, accuracyInMeters: Float) {}
}