package com.example.bloold.buildp.catalog.`object`

import android.os.AsyncTask
import android.text.TextUtils
import android.util.ArraySet
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.bloold.buildp.JSONTask
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
import kotlin.collections.ArrayList

/**
 * Created by bloold on 18.10.17.
 */

class CatalogObjectsPresenter(private val view: callback) {

    fun getCatalogObjects(url: String) {
        JSONTask(view).execute(url)
    }
}