package com.jdy.android.fortube.map

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import com.jdy.android.fortube.R
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MarkerMapView(context: Context, attrs: AttributeSet): MapView(context, attrs), MarkerMapViewEventListener {
    companion object {
        const val MAP_CURRENT_LOCATION_DIFF = 100   // meters
        const val MAP_CENTER_MOVED_TIME_DIFF = 300   // milliseconds
    }
    private lateinit var mMapViewModel: MapViewModel
    private var mAllowMapRefreshListener: OnAllowMapRefreshListener? = null
    private var mIsAllowedShowRefresh = false
    private var mLastLocation = Location("")
    private var mLastCenterMovedTime = System.currentTimeMillis()

    @FunctionalInterface
    interface OnAllowMapRefreshListener {
        fun allowRefresh()
    }

    fun setMapViewModel(mapViewModel: MapViewModel) {
        mMapViewModel = mapViewModel
    }

    fun setOnAllowMapRefreshListener(listener: OnAllowMapRefreshListener) {
        mAllowMapRefreshListener = listener
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

    fun selectMarker(document: MapDocument) {
        val markers = findPOIItemByName(document.placeName)
        if (markers.isNotEmpty()) {
            selectPOIItem(markers[0], true)
        }
        setMapCenterPoint(MapPoint.mapPointWithGeoCoord(document.y.toDouble(), document.x.toDouble()), true)
    }

    fun refresh() {
        mIsAllowedShowRefresh = false
        mMapViewModel.refreshMapAreas(mapCenterPoint.mapPointGeoCoord)
    }

    fun update() {
        mMapViewModel.updateMapAreas()
    }

    fun setMarkerList(documents: List<MapDocument>) {
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
            isShowDisclosureButtonOnCalloutBalloon = false
        }
    }

    private fun isAllowedShowRefresh(point: MapPoint): Boolean {
        if (mIsAllowedShowRefresh) return false
        val curLocation = Location("").apply {
            latitude = point.mapPointGeoCoord.latitude
            longitude = point.mapPointGeoCoord.longitude
        }
        if (mLastLocation.distanceTo(curLocation) > MAP_CURRENT_LOCATION_DIFF) {
            mLastLocation = curLocation
            mIsAllowedShowRefresh = true
            return true
        }
        return false
    }

    override fun onMapViewMoveFinished(mapView: MapView, point: MapPoint) {
        if (poiItems.isEmpty()) {
            refresh()
        }
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, point: MapPoint) {
        if (mIsAllowedShowRefresh) return
        val curCenterMovedTime = System.currentTimeMillis()
        if (curCenterMovedTime - mLastCenterMovedTime > MAP_CENTER_MOVED_TIME_DIFF) {
            mLastCenterMovedTime = curCenterMovedTime
            if (isAllowedShowRefresh(point)) {
                mAllowMapRefreshListener?.allowRefresh()
            }
        }
    }

    override fun onPOIItemSelected(mapView: MapView, item: MapPOIItem) {
        setMapCenterPoint(item.mapPoint, true)
    }

    override fun onCurrentLocationUpdate(mapView: MapView, point: MapPoint, accuracyInMeters: Float) {
        currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
        mLastLocation.latitude = point.mapPointGeoCoord.latitude
        mLastLocation.longitude = point.mapPointGeoCoord.longitude
        setMapCenterPointAndZoomLevel(point, 4, true)
    }
}