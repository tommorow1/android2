package com.example.bloold.buildp.api

import com.example.bloold.buildp.api.data.BaseResponse
import retrofit2.Response

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 26.09.17.
 */

object ErrorUtils {
    fun parseError(response: Response<*>?): BaseResponse<*>? {
        val converter = ServiceGenerator.retrofit(ServiceGenerator.BASE_URL)
                .responseBodyConverter<BaseResponse<*>>(BaseResponse::class.java, arrayOfNulls(0))

        if (response?.body() != null && response.body() is BaseResponse<*>) {
            val errorResponse = response.body() as BaseResponse<*>?
            if (errorResponse != null)
                return errorResponse
        }

        try {
            return converter.convert(response!!.errorBody())
        } catch (e: Exception) {
        }
        return null
    }
}
