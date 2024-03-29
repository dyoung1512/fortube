package com.jdy.android.fortube.map


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.daum.mf.map.api.MapPoint

class MapViewModel(private val mMapService: MapService): ViewModel() {
    companion object {
        const val MAP_LOCATION_SEARCH_RADIUS = 2000   // meters
        const val CD_CATEGORY_OIL = "OL7"
        const val CD_CATEGORY_HOSPITAL = "HP8"
        const val CD_CATEGORY_PHARMACY = "PM9"
        const val STATE_CATEGORY_EMPTY = 0
        const val STATE_CATEGORY_OIL = 1
        const val STATE_CATEGORY_HOSPITAL = 2
        const val STATE_CATEGORY_PHARMACY = 4
    }
    private var mCategoryState = STATE_CATEGORY_EMPTY
    private var mDisposable = CompositeDisposable()
    private var mLastGeoCoord: MapPoint.GeoCoordinate? = null

    var page = 1
    var mapData = MutableLiveData<MapModel>()
    var mapItemSelect = MutableLiveData<MapDocument>()

    fun refreshMapAreas(geoCoord: MapPoint.GeoCoordinate) {
        mLastGeoCoord = geoCoord
        if (isCategoryEmpty()) {
            mapData.value?.run {
                documents.clear()
                mapData.postValue(this)
            }
            return
        }
        page = 1
        mDisposable.add(mMapService.searchMapAreas(
            getCategories(),
            geoCoord.longitude.toString(),
            geoCoord.latitude.toString(),
            MAP_LOCATION_SEARCH_RADIUS,
            page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.trampoline())
            .subscribe(
                mapData::postValue,
                Throwable::printStackTrace
            ))
    }

    fun updateMapAreas() {
        if (isCategoryEmpty() || mLastGeoCoord == null) {
            mapData.value?.run {
                documents.clear()
                mapData.postValue(this)
            }
            return
        }
        page += 1
        mDisposable.add(mMapService.searchMapAreas(
            getCategories(),
            mLastGeoCoord!!.longitude.toString(),
            mLastGeoCoord!!.latitude.toString(),
            MAP_LOCATION_SEARCH_RADIUS,
            page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.trampoline())
            .subscribe(
                { mapModel ->
                    mapData.postValue(mapModel.let { newModel ->
                        mapData.value?.documents?.run {
                            addAll(newModel.documents)
                            newModel.documents = this
                            return@let newModel
                        } ?: newModel
                    })
                },
                Throwable::printStackTrace
            ))
    }

    fun onSelectMapItem(document: MapDocument) {
        mapItemSelect.value = document
    }

    fun setCategoryState(state: Int) { mCategoryState = state }
    fun setCategoryOil(set: Boolean) = setCategoryState(set, STATE_CATEGORY_OIL)
    fun setCategoryHospital(set: Boolean) = setCategoryState(set, STATE_CATEGORY_HOSPITAL)
    fun setCategoryPharmacy(set: Boolean) = setCategoryState(set, STATE_CATEGORY_PHARMACY)
    private fun setCategoryState(set: Boolean, category: Int) {
        mCategoryState = when (set) {
            true -> mCategoryState or category
            false -> mCategoryState xor category
        }
    }

    private fun getCategories(): String {
        return StringBuilder().apply {
            if (hasCategoryOil()) {
                append(CD_CATEGORY_OIL)
            }
            if (hasCategoryHospital()) {
                if (isNotEmpty()) append(",")
                append(CD_CATEGORY_HOSPITAL)
            }
            if (hasCategoryPharmacy()) {
                if (isNotEmpty()) append(",")
                append(CD_CATEGORY_PHARMACY)
            }
        }.toString()
    }
    private fun isCategoryEmpty() = mCategoryState == STATE_CATEGORY_EMPTY
    private fun hasCategoryOil() = mCategoryState and STATE_CATEGORY_OIL == STATE_CATEGORY_OIL
    private fun hasCategoryHospital() = mCategoryState and STATE_CATEGORY_HOSPITAL == STATE_CATEGORY_HOSPITAL
    private fun hasCategoryPharmacy() = mCategoryState and STATE_CATEGORY_PHARMACY == STATE_CATEGORY_PHARMACY

    override fun onCleared() {
        super.onCleared()
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }
}