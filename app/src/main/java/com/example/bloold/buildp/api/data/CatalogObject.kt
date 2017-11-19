package com.example.bloold.buildp.api.data

import com.example.bloold.buildp.model.PhotoModel
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
    @get: JsonProperty("DETAIL_PICTURE")
    var detailPicture: PhotoModel?=null
    /*@get: JsonProperty("PHOTOS_DATA")
    var photosData: Object =""
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
}