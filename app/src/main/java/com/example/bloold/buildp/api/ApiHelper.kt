package com.example.bloold.buildp.api

/**
 * Created by sagus on 18.11.2017.
 */
class ApiHelper {
    companion object {
        val defaultParams:Array<String> by lazy { arrayOf("ID", "NAME","DETAIL_PICTURE","IS_FAVORITE")}
        val mapParams:Array<String> by lazy { arrayOf("OBJECT_ID", "NAME","ADDRESS","MAP_TYPE","LAT","LNG")}
        val fullParams:Array<String> by lazy { defaultParams+arrayOf("PREVIEW_TEXT", "PROPERTY_ADDRESS",
                "PHOTOS_DATA",
                "DOCS_DATA",
                "PUBLICATIONS_DATA",
                "VIDEO_DATA",
                "AUDIO_DATA",
                "DETAIL_PAGE_URL",
                "PROPERTY_MAP"
        )}
/*
        private val URL = "http://ruinnet.idefa.ru/api_app/object/list/?select[]=ID&select[]=NAME&select[]=PREVIEW_TEXT&select[]=" +
                "PROPERTY_ADDRESS&select[]=DETAIL_PICTURE&select[]=PHOTOS_DATA&select[]=DOCS_DATA&select[]=PUBLICATIONS_DATA&select[]=" +
                "VIDEO_DATA&select[]=AUDIO_DATA&select[]=DETAIL_PAGE_URL&select[]=IS_FAVORITE&" +
                "select[]=PROPERTY_MAP=Y&filter[INCLUDE_SUBSECTIONS]=Y&filter[ID][0]="
*/

    }
}