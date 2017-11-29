package com.example.bloold.buildp.api

import com.example.bloold.buildp.model.VideoModel


/**
 * Created by sagus on 18.11.2017.
 */
class ApiHelper {
    companion object {
        val defaultParams:Array<String> by lazy { arrayOf("ID", "NAME","DETAIL_PICTURE","IS_FAVORITE")}
        val mapParams:Array<String> by lazy { arrayOf("OBJECT_ID", "NAME","ADDRESS","MAP_TYPE","LAT","LNG")}
        val fullParams:Array<String> by lazy { defaultParams+arrayOf(
                "PREVIEW_TEXT",
                "PROPERTY_ADDRESS",
                "PHOTOS_DATA",
                "DOCS_DATA",
                "PUBLICATIONS_DATA",
                "VIDEO_DATA",
                "AUDIO_DATA",
                "DETAIL_PAGE_URL",
                "PROPERTY_MAP",
                "PROPERTY_UNESCO",
                "PROPERTY_TYPE",
                "PROPERTY_VALUE_CATEGORY",
                "PROPERTY_CONDITION",//Описание состояния
                "CONDITION_ID"//Оценка состояния
        )}
        fun generateUploadFileParams(name:String, base64FileData:String, key: Long)
                = HashMap<String,String>()
                    .apply { put("FILES[$key][DATA]", base64FileData) }
                    .apply { put("FILES[$key][NAME]", name) }
        fun generateChangedPhotosParams(photoFiles:List<Long>):HashMap<String,String>
                =HashMap<String,String>().apply { photoFiles.forEach {
                put("FORM[PROPS][PHOTOS][$it][DETAIL_PICTURE]", it.toString())
                put("FORM[PROPS][PHOTOS][$it][CHANGED]", "1") }}
        fun generateChangedAudiosParams(photoFiles:List<Long>):HashMap<String,String>
                =HashMap<String,String>().apply { photoFiles.forEach {
            put("FORM[PROPS][AUDIO][$it][PROPS][FILE]", it.toString())
            put("FORM[PROPS][AUDIO][$it][CHANGED]", "1") }}
        fun generateArchiveDocsParams(archiveDocsFiles:List<Long>):HashMap<String,String>
                =HashMap<String,String>().apply { archiveDocsFiles.forEach {
            put("FORM[PROPS][DOCS][$it][PROPS][FILE][VALUE]", it.toString())
            put("FORM[PROPS][DOCS][$it][CHANGED]", "1") }}
        fun generatePublicationParams(archiveDocsFiles:List<Long>):HashMap<String,String>
                =HashMap<String,String>().apply { archiveDocsFiles.forEach {
            put("FORM[PROPS][PUBLICATIONS][$it][PROPS][FILE][VALUE]", it.toString())
            put("FORM[PROPS][PUBLICATIONS][$it][CHANGED]", "1") }}
        fun generateChangedVideoParams(videoForUploading:List<VideoModel>):HashMap<String,String>
        {
            val data = HashMap<String,String>()
            for (i in 0 until videoForUploading.size)
            {
                videoForUploading[i].youtubeCode?.let {
                    data.put("FORM[PROPS][VIDEO][$i][CODE]", it)
                    data.put("FORM[PROPS][VIDEO][$i][CHANGED]", "1")
                }
            }
            return data
        }
    }
}