package com.jdy.android.fortube.map

import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    @GET("/v2/local/search/category.json")
    fun searchAreas(
        @Query("category_group_code") categories: String,
        @Query("rect") rect: String)
}