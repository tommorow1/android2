package com.example.bloold.buildp.catalog.`object`

import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.CatalogObjectsModel
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
import java.util.ArrayList

/**
 * Created by bloold on 18.10.17.
 */

interface CatalogPresenterListener {
    fun onObjectsLoaded(items: List<CatalogObjectsModel>)
}

class CatalogObjectsPresenter(private val view: CatalogPresenterListener) {

    fun getCatalogObjects(url: String) {
        JSONTask().execute(url)
    }

    inner class JSONTask : AsyncTask<String, String, List<CatalogObjectsModel>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            //dialog.show()
        }

        override fun doInBackground(vararg params: String): List<CatalogObjectsModel>? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Device-Id", "0000")

                val header = "Basic " + String(android.util.Base64.encode("defa:defa".toByteArray(), android.util.Base64.NO_WRAP))
                connection.addRequestProperty("Authorization", header)
                connection.connect()

                val stream = connection.inputStream
                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuffer()
                var line: String? = ""

                line = reader.readLine()

                while (line != null && line != "") {
                    buffer.append(line)
                    line = reader.readLine()
                }

                val finalJson = buffer.toString()
                Log.d("json", finalJson)
                var ITEMS: JSONObject? = null
                var DATA: JSONObject? = null
                var response: JSONObject? = null
                response = JSONObject(finalJson)

                val parentArray: JSONArray? = null

                try {
                    DATA = JSONObject(response.getString("DATA"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                try {
                    ITEMS = JSONObject(DATA!!.getString("ITEMS"))
                    //parentArray = DATA.getJSONArray("ITEMS");
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                val catalogModelList = ArrayList<CatalogObjectsModel>()

                val gson = Gson()
                val array: JSONArray? = null

                val iterator = ITEMS!!.keys()
                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                    val finalObject = ITEMS.getJSONObject(key)
                    val catalogModel = CatalogObjectsModel()

                    try {
                        catalogModel.name = finalObject.getString("NAME")
                        catalogModel.property_address = finalObject.getString("PROPERTY_ADDRESS")

                        val ImgObject: JSONObject? = finalObject.getJSONObject("DETAIL_PICTURE")

                        if(ImgObject != null)
                            catalogModel.src = "http://ruinnet.idefa.ru/" + ImgObject.getString("SRC")

                        catalogModel.preview_text = finalObject.getString("PREVIEW_TEXT")
                    } catch (e: JSONException){
                        e.printStackTrace()
                    }

                    catalogModelList.add(catalogModel)
                }
                return catalogModelList

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(result: List<CatalogObjectsModel>?) {
            super.onPostExecute(result)
            //dialog.dismiss()
            if (result != null) {
                view.onObjectsLoaded(result)
                Log.d("result", "onPostExecute")
            } else {
                //Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }
}