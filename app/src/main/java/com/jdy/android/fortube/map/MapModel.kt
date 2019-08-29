package com.jdy.android.fortube.map

import com.google.gson.annotations.SerializedName
import com.jdy.android.fortube.base.Item

class MapModel(
    @SerializedName("meta") var meta: MapMeta,
    @SerializedName("documents") var documents: ArrayList<MapDocument>)
class MapMeta(
    @SerializedName("total_count") var totalCount: Int,
    @SerializedName("pageable_count") var pageableCount: Int,
    @SerializedName("is_end") var isEnd: Boolean)
class MapDocument(
    @SerializedName("id") var id: String,
    @SerializedName("place_name") var placeName: String,
    @SerializedName("address_name") var addressName: String,
    @SerializedName("road_address_name") var roadAddressName: String,
    @SerializedName("category_group_code") var categoryGroupCode: String,
    @SerializedName("category_group_name") var categoryGroupName: String,
    @SerializedName("x") var x: String,
    @SerializedName("y") var y: String): Item