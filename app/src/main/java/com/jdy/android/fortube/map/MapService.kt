package com.jdy.android.fortube.map

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    /**
     * category_group_code : 카테고리 그룹 (OL7 - 주유소, 충전소, HP8 - 병원, PM9 - 약국)
     * x : 중심 좌표의 X값 혹은 longitude. 특정 지역을 중심으로 검색하려고 할 경우 radius와 함께 사용
     * y : 중심 좌표의 Y값 혹은 latitude. 특정 지역을 중심으로 검색하려고 할 경우 radius와 함께 사용
     * radius : 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 중심좌표로 쓰일 x,y와 함께 사용. 단위 meter
     * page : 페이지 번호, 페이지 당 조회되는 아이템 수 = 15 (default)
     */
    @GET("/v2/local/search/category.json")
    fun searchMapAreas(
        @Query("category_group_code") categories: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int,
        @Query("page") page: Int): Single<MapModel>
}