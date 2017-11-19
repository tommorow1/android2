package com.example.bloold.buildp.api

import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.CatalogObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 24.05.16.
 */
interface ServerApi {
    @GET("object/list/")
    fun getCatalogObjects(@QueryMap filter:Map<String, String>?,
                          @Query("limit") limit: Int, @Query("page") page: Int,
                          @Query("select[]") selectParams: Array<String> = ApiHelper.defaultParams): Single<BaseResponse<CatalogObject>>
}
