package com.jdy.android.fortube.map

import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

interface MarkerMapViewEventListener: MapView.MapViewEventListener, MapView.POIItemEventListener {
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewInitialized(p0: MapView?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    // ------------------------------------------------------------------------
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
//    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}

}