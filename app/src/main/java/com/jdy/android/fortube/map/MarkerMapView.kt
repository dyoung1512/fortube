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
        const val MAP_CURRENT_LOCATION_DIFF = 50   // meters
    }
    private lateinit var mMapViewModel: MapViewModel
    private var mAllowMapRefreshListener: OnAllowMapRefreshListener? = null
    private var mIsCenterPointMoved = false
    private var mLastLocation = Location("")

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

    fun refresh() {
        if (!mMapViewModel.isCategoryEmpty()) {
            mMapViewModel.searchMapAreas(mapCenterPoint.mapPointGeoCoord)
        } else if (poiItems.isNotEmpty()) {
            removeAllPOIItems()
        }
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
        mLastLocation.latitude = mapCenterPoint.mapPointGeoCoord.latitude
        mLastLocation.longitude = mapCenterPoint.mapPointGeoCoord.longitude
    }

    override fun onMapViewMoveFinished(mapView: MapView, point: MapPoint) {
        if (poiItems.isEmpty()) {
            refresh()
        }
        val curLocation = Location("").apply {
            latitude = point.mapPointGeoCoord.latitude
            longitude = point.mapPointGeoCoord.longitude
        }
        if (mIsCenterPointMoved && mLastLocation.distanceTo(curLocation) > MAP_CURRENT_LOCATION_DIFF) {
            mIsCenterPointMoved = false
            mLastLocation = curLocation
            mAllowMapRefreshListener?.allowRefresh()
        }
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, point: MapPoint) {
        mIsCenterPointMoved = true
    }

    override fun onPOIItemSelected(mapView: MapView, item: MapPOIItem) {
        mIsCenterPointMoved = true
        setMapCenterPoint(item.mapPoint, true)
    }

    override fun onCurrentLocationUpdate(mapView: MapView, point: MapPoint, accuracyInMeters: Float) {
        currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
        mLastLocation.latitude = point.mapPointGeoCoord.latitude
        mLastLocation.longitude = point.mapPointGeoCoord.longitude
        setMapCenterPoint(point, true)
    }
}