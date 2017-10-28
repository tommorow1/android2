package com.example.bloold.buildp.search

import android.content.Context
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import com.example.bloold.buildp.ListCatalogObjectsResponse
import com.example.bloold.buildp.R
import com.example.bloold.buildp.callback
import com.example.bloold.buildp.model.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by bloold on 21.10.17.
 */
class SearchPresenter(private val view: callback, private val context: Context) {

    fun findAddress(seq: String){
        var url = ""

        val egrn = seq.toIntOrNull()
        if(egrn != null) {
            url = getEGRN(url)
        } else {
            url = getAddress(seq)
        }

        ListCatalogObjectsResponse(view).execute(url)
    }

    fun findObjects(seq: String){
        ListCatalogObjectsResponse(view).execute(getObject(seq))
    }

    private fun getEGRN(egrn: String): String{
        return "http://ruinnet.idefa.ru/api_app/object/list/?select[]=ID&amp;select[]=NAME&amp;select[]=PREVIEW_TEXT&amp;select[]=PROPERTY_ADDRESS&amp;select[]=DETAIL_PICTURE&amp;select[]=PHOTOS_DATA&amp;select[]=DOCS_DATA&amp;select[]=PUBLICATIONS_DATA&amp;select[]=VIDEO_DATA&amp;select[]=AUDIO_DATA&amp;select%5B7%5D=PROPERTY_EGRKN_NUMBER&amp;filter%5BPROPERTY_EGRKN_NUMBER%5D=" + egrn
    }

    private fun getAddress(address: String): String{
        return context.resources.getString(R.string.find_by_address, address)
    }

    private fun getObject(obj: String): String{
        return context.resources.getString(R.string.find_by_name, obj)
    }
}