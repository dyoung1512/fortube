package com.jdy.android.fortube.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.jdy.android.fortube.R
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
        setCustomCurrentLocationMarkerImage(R.drawable.ic_map_current_red, MapPOIItem.ImageOffset(18, 18))
        setMapViewEventListener(this)
        setPOIItemEventListener(this)
        setCurrentLocationEventListener(this)
    }

    fun setCurrentLocation() {
        currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    fun refresh() {
        mMapViewModel.searchMapAreas(mapCenterPoint.mapPointGeoCoord)
    }

    fun addMarkerList(documents: List<MapDocument>) {
        removeAllPOIItems()
        for (document in documents) {
            addPOIItem(convertDocumentToMapPOIItem(document))
        }
    }

    private fun convertDocumentToMapPOIItem(document: MapDocument): MapPOIItem {
        return MapPOIItem().apply {
            itemName = document.placeName
            when (document.categoryGroupCode) {
                MapViewModel.CD_CATEGORY_OIL -> tag = MapViewModel.STATE_CATEGORY_OIL
                MapViewModel.CD_CATEGORY_HOSPITAL -> tag = MapViewModel.STATE_CATEGORY_HOSPITAL
                MapViewModel.CD_CATEGORY_PHARMACY -> tag = MapViewModel.STATE_CATEGORY_PHARMACY
            }
            mapPoint = MapPoint.mapPointWithGeoCoord(document.y.toDouble(), document.x.toDouble())
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
            isShowDisclosureButtonOnCalloutBalloon = false
        }
    }

    override fun onMapViewInitialized(mapView: MapView) {
        Log.d("MarkerMapView", "onMapViewInitialized")
        Log.i("MarkerMapView", "mapCenterPoint (${mapCenterPoint.mapPointGeoCoord.latitude}, ${mapCenterPoint.mapPointGeoCoord.longitude})")
    }

    override fun onMapViewMoveFinished(mapView: MapView, point: MapPoint) {
        Log.i("MarkerMapView", "onMapViewMoveFinished (${point.mapPointGeoCoord.latitude}, ${point.mapPointGeoCoord.longitude})")
        if (poiItems.isEmpty()) {
            refresh()
        }
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, point: MapPoint) {
        Log.i("MarkerMapView", "onMapViewCenterPointMoved (${point.mapPointGeoCoord.latitude}, ${point.mapPointGeoCoord.longitude})")
    }

    override fun onPOIItemSelected(mapView: MapView, item: MapPOIItem) {
        setMapCenterPoint(item.mapPoint, true)
    }

    override fun onCurrentLocationUpdate(mapView: MapView, point: MapPoint, accuracyInMeters: Float) {
        Log.w("MarkerMapView", "onCurrentLocationUpdate [(${point.mapPointGeoCoord.latitude}, ${point.mapPointGeoCoord.longitude}) - $accuracyInMeters]")
        currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
        setMapCenterPoint(point, true)
    }
}