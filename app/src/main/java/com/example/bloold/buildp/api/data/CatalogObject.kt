package com.example.bloold.buildp.api.data

import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.model.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by sagus on 18.11.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class CatalogObject {
    @get: JsonProperty("ID")
    var id = 0
    @get: JsonProperty("NAME")
    var name: String =""
    @get: JsonProperty("CODE")
    var code: String =""
    @get: JsonProperty("PROPERTY_ADDRESS")
    var propertyAddress: String?=null
    @get: JsonProperty("PREVIEW_TEXT")
    var previewText: String?=null
    @get: JsonProperty("DETAIL_PAGE_URL")
    var detailPageUrl: String?=null
    @get: JsonProperty("DETAIL_PICTURE")
    var detailPicture: DetailPictureModel?=null
    @get: JsonProperty("PHOTOS_DATA")
    var photosData: Array<PhotoModel>?=null
    @get: JsonProperty("DOCS_DATA")
    var docsData: Array<DocModel>?=null
    @get: JsonProperty("VIDEO_DATA")
    var videoData: Array<VideoModel>?=null
    @get: JsonProperty("AUDIO_DATA")
    var audioData: Array<AudioModel>?=null
    @get: JsonProperty("PUBLICATIONS_DATA")
    var publicationsData: Array<PublicationsModel>?=null
    @get: JsonProperty("PROPERTY_MAP")
    var propertyMap: String?=null
    @get: JsonProperty("IS_FAVORITE")
    var isFavourite: Boolean? = false
    @get: JsonProperty("PROPERTY_CONDITION")
    var condition: String? = null //Состояние объекта
    @get: JsonProperty("PROPERTY_UNESCO")
    var isUnesco: String? = null
    @get: JsonProperty("PROPERTY_VALUABLE")
    var isValuable: String? = null
    @get: JsonProperty("PROPERTY_TYPE")
    var propertyType: String? = null
    @get: JsonProperty("PROPERTY_VALUE_CATEGORY")
    var propertyCategory: String? = null
    @get: JsonProperty("PROPERTY_LAT")
    var latitude: Double? = null
    @get: JsonProperty("PROPERTY_LNG")
    var longitude: Double? = null
    /*
    @get: JsonProperty("DOCS_DATA")
    var docsData: Object =""
    @get: JsonProperty("PUBLICATIONS_DATA")
    var publicationsData: Object =""
    @get: JsonProperty("VIDEO_DATA")
    var videoData: Object =""
    @get: JsonProperty("AUDIO_DATA")
    var audioData: Object =""
    @get: JsonProperty("IS_FAVORITE")
    var isFavourite: Object =""*/
    fun getFullLink(): String?
            = if(detailPageUrl!=null) ServiceGenerator.SITE_URL+detailPageUrl else null
}