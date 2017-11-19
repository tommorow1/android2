package com.example.bloold.buildp.single.`object`

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bloold.buildp.R
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.databinding.ActivitySingleObjectBinding
import com.example.bloold.buildp.model.*
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
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SingleObjectActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySingleObjectBinding
    private var item: CatalogObjectsModel? = null
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: CollapsingToolbarLayout
    private lateinit var ivAvatar: ImageView

    private lateinit var tvTitleToolbar: TextView
    private lateinit var ivBackToolbar: ImageView
    private lateinit var ivStarToolbar: ImageView

    private lateinit var tvDistance: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvTitle: TextView
    private val URL = "http://ruinnet.idefa.ru/api_app/object/list/?select[]=ID&select[]=NAME&select[]=PREVIEW_TEXT&select[]=PROPERTY_ADDRESS&select[]=DETAIL_PICTURE&select[]=PHOTOS_DATA&select[]=DOCS_DATA&select[]=PUBLICATIONS_DATA&select[]=VIDEO_DATA&select[]=AUDIO_DATA&select[]=DETAIL_PAGE_URL&select[]=IS_FAVORITE&select[]=PROPERTY_MAP=Y&filter[INCLUDE_SUBSECTIONS]=Y&filter[ID][0]="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_object)

        GetObject(this).execute(URL+intent.getIntExtra(IntentHelper.EXTRA_OBJECT_ID, 0).toString())

        /*if(intent.hasExtra(EXTRA_OBJECT_KEY) ?: false){
            item = intent.getParcelableExtra<CatalogObjectsModel>(EXTRA_OBJECT_KEY)

            for(i: PhotoModel in item?.photos!!){
                Log.d("photos", i.src)
            }
        }*/

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        toolbar = findViewById<CollapsingToolbarLayout>(R.id.ctlMainSingleObject)

        tvTitleToolbar = findViewById(R.id.tvTitle)
        ivBackToolbar = findViewById(R.id.ivBack)
        ivStarToolbar = findViewById(R.id.ivStarFill)

        tvTitle = findViewById(R.id.tvName)
        tvAddress = findViewById(R.id.tvAddress)
        tvDistance = findViewById(R.id.tvDistance)

        ivAvatar = findViewById<ImageView>(R.id.ivAvatar)

        viewPager = findViewById(R.id.vpSingleObject)

    }

    //companion object {
       // val EXTRA_OBJECT_KEY = "object"
   // }
    inner class GetObject(val context: Context): AsyncTask<String, String, ArrayList<CatalogObjectsModel>>() {

            override fun onPreExecute() {
            super.onPreExecute()
            //dialog.show()
        }

        override fun doInBackground(vararg params: String): ArrayList<CatalogObjectsModel>? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            Log.d("doinback", params[0])

            try {
                val url = URL(params[0]+"&limit=1&page=1")
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
                var objects = ArrayList<CatalogObjectsModel>()

                try {
                    objects = getCatalogObject(finalJson)
                } catch (e: Exception){
                    e.printStackTrace()
                }

                return objects

            } catch (e: MalformedURLException) {
                //e.printStackTrace()
            } catch (e: IOException) {
                //e.printStackTrace()
            } catch (e: JSONException) {
                //e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    //e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(result: ArrayList<CatalogObjectsModel>?) {
            super.onPostExecute(result)
            //dialog.dismiss()
            if(result != null) {
                item = result.get(result.size-1)
                Glide.with(context).load(item?.src).into(ivAvatar)
                Glide.with(context)
                        .load(item?.src)
                        .into(object: SimpleTarget<Drawable>(resources.displayMetrics.widthPixels, 100){
                            override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
                                toolbar.contentScrim = resource
                            }
                        })

                ivBackToolbar.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(v: View?) {
                        onBackPressed()
                    }
                })

                tvTitle.text = item!!.name
                tvDistance.text = "250m"
                tvAddress.text = item?.property_address

                tvTitleToolbar.text = item!!.name

                pagerAdapter = PagerSingleObjectAdapter(item!!, supportFragmentManager)
                viewPager.adapter = pagerAdapter

                tabLayout = findViewById(R.id.tabs)
                tabLayout.setupWithViewPager(viewPager)

                findViewById<AppBarLayout>(R.id.appbar).addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener{
                    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                        if(appBarLayout!!.totalScrollRange + verticalOffset < 100){
                            tvTitleToolbar.visibility = View.VISIBLE
                            ivStarToolbar.visibility = View.VISIBLE
                        } else {
                            tvTitleToolbar.visibility = View.INVISIBLE
                            ivStarToolbar.visibility = View.INVISIBLE
                        }
                    }

                })
            }
        }

        fun getCatalogObject(responseStr: String): ArrayList<CatalogObjectsModel> {

            val response = JSONObject(responseStr)

            var ITEMS: JSONArray? = null
            var DATA: JSONObject? = null

            val parentArray: JSONArray? = null

            try {
                DATA = JSONObject(response.getString("DATA"))
            } catch (e: JSONException) {
                //e.printStackTrace()
            }

            try {
                ITEMS = JSONArray(DATA!!.getString("ITEMS"))
                //parentArray = DATA.getJSONArray("ITEMS");
            } catch (e: JSONException) {
                //e.printStackTrace()
            }

            val catalogModelList = ArrayList<CatalogObjectsModel>()
            val array: JSONArray? = null

            for(i:Int in 0..(ITEMS?.length() ?: 1) - 1) {
                val finalObject = ITEMS!!.getJSONObject(i)
                Log.d("i: ${i}", finalObject.toString())
                val catalogModel = CatalogObjectsModel()

                try {
                    catalogModel.id = finalObject.getString("ID")
                    catalogModel.name = finalObject.getString("NAME")

                    val ImgObject: JSONObject? = finalObject.getJSONObject("DETAIL_PICTURE")

                    if (ImgObject != null)
                        catalogModel.src = "http://ruinnet.idefa.ru/" + ImgObject.getString("SRC")

                    catalogModel.preview_text = finalObject.getString("PREVIEW_TEXT")

                    Log.d("tag", catalogModel.preview_text)

                    catalogModel.property_address = finalObject.getString("PROPERTY_ADDRESS")


                    //catalogModel.audios = finalObject.getJSONArray("AUDIO_DATA").toString()

                    //catalogModel.docs = finalObject.getJSONArray("DOCS_DATA")
                    //catalogModel.publications = finalObject.getJSONArray("PUBLICATIONS_DATA")
                    //catalogModel.videos = finalObject.getJSONArray("VIDEO_DATA")
                    catalogModel.isFavorite = finalObject.getBoolean("IS_FAVORITE")
                } catch (e: JSONException) {

                }

                try {
                    //PHOTO
                    var photos = ArrayList<PhotoModel>()

                    val photosJsonArray = finalObject.getJSONArray("PHOTOS_DATA")

                    for (i: Int in 0..photosJsonArray.length() - 1) {
                        val photo = PhotoModel()
                        photo.name = photosJsonArray.getJSONObject(i).getString("NAME")
                        photo.src = "http://ruinnet.idefa.ru/" +
                                photosJsonArray.getJSONObject(i)
                                        .getJSONObject("DETAIL_PICTURE")
                                        .getString("SRC")
                        if (!photo.src.isNullOrEmpty()) {
                            photos.add(photo)
                        }
                    }

                    catalogModel.photos = photos

                    //VIDEO
                    var videos = ArrayList<VideoModel>()

                    val videosJsonArray = finalObject.getJSONArray("VIDEO_DATA")

                    for(i: Int in 0..videosJsonArray.length() - 1){
                        val video = VideoModel()
                        video.name = videosJsonArray.getJSONObject(i).getString("NAME")
                        video.code = "http://ruinnet.idefa.ru/" +
                                videosJsonArray.getJSONObject(i).getString("CODE")

                        if(!video.code.isNullOrEmpty()){
                            videos.add(video)
                        }
                    }

                    catalogModel.videos = videos.toTypedArray()

                    //AUDIO
                    var audios = ArrayList<AudioModel>()

                    val audiosJsonArray = finalObject.getJSONArray("AUDIO_DATA")

                    for(i: Int in 0..audiosJsonArray.length() - 1){
                        val audio = AudioModel()
                        audio.name = audiosJsonArray .getJSONObject(i).getString("NAME")
                        audio.src = "http://ruinnet.idefa.ru/" +
                                audiosJsonArray .getJSONObject(i).getString("SRC")
                        if(!audio.src.isNullOrEmpty()){
                            audios.add(audio)
                        }
                    }

                    catalogModel.audios = audios.toTypedArray()

                    //DOCS
                    var docs = ArrayList<DocModel>()

                    val docsJsonArray = finalObject.getJSONArray("AUDIO_DATA")

                    for(i: Int in 0..docsJsonArray.length() - 1){
                        val doc = DocModel()
                        doc.name = docsJsonArray.getJSONObject(i).getString("NAME")
                        doc.code = "http://ruinnet.idefa.ru/" +
                                docsJsonArray.getJSONObject(i).getString("SRC")
                        if(!doc.code.isNullOrEmpty()){
                            docs.add(doc)
                        }
                    }

                    catalogModel.docs = docs.toTypedArray()

                } catch (e: JSONException){
                    //e.printStackTrace()
                }

                Log.d("i: ${i}", catalogModel.name.toString())
                catalogModelList.add(catalogModel)
            }

            Log.d("${catalogModelList.size}", catalogModelList.toString())
            return catalogModelList
        }
    }
}
