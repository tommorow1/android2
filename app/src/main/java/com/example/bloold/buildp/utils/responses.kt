package com.example.bloold.buildp.utils

import com.example.bloold.buildp.model.CatalogObjectsModel
import com.google.gson.annotations.SerializedName

/**
 * Created by bloold on 26.10.17.
 */
class BaseResponse<out T>(val message: String?,
                          @SerializedName("DATA")
                          val data: T?)

class Items(@SerializedName("ITEMS")
        val items: List<CatalogObjectsModel>)