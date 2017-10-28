package com.example.bloold.buildp.service

import com.example.bloold.buildp.utils.BaseResponse
import com.example.bloold.buildp.utils.Items
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

/**
 * Created by bloold on 26.10.17.
 */
interface SearchService {

    @GET("/object/list/?select%5B0%5D=NAME&amp;select%5B7%5D=PROPERTY_EGRKN_NUMBER&amp;filter%5BPROPERTY_EGRKN_NUMBER%5D={egrn}")
    fun getHomeFeed(@Path(value = "egrn", encoded = true) egrn: String): Single<BaseResponse<Items>>
}