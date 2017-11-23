package com.example.bloold.buildp.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bloold.buildp.R
import com.example.bloold.buildp.adapter.CatalogObjectDetailsPagerAdapter
import com.example.bloold.buildp.api.ApiHelper
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponseWithDataObject
import com.example.bloold.buildp.api.data.CatalogObject
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.common.RxHelper
import com.example.bloold.buildp.common.Settings
import com.example.bloold.buildp.components.EventActivity
import com.example.bloold.buildp.databinding.ActivityCatalogObjectDetailsBinding
import com.example.bloold.buildp.services.NetworkIntentService
import io.reactivex.observers.DisposableSingleObserver
import java.net.ConnectException
import java.net.UnknownHostException


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CatalogObjectDetailsActivity : EventActivity() {
    private lateinit var mBinding: ActivityCatalogObjectDetailsBinding
    private var catalogObject: CatalogObject? = null
    private var isFavourite = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_catalog_object_details)
        mBinding.listener=this
        //GetObject(this).execute(URL+intent.getIntExtra(IntentHelper.EXTRA_OBJECT_ID, 0).toString())

        /*if(intent.hasExtra(EXTRA_OBJECT_KEY) ?: false){
            item = intent.getParcelableExtra<CatalogObjectsModel>(EXTRA_OBJECT_KEY)

            for(i: PhotoModel in item?.photos!!){
                Log.d("photos", i.src)
            }
        }*/

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        toolbar = findViewById(R.id.ctlMainSingleObject)

        tvTitleToolbar = findViewById(R.id.tvTitle)
        ivBackToolbar = findViewById(R.id.ivBack)
        ivStarToolbar = findViewById(R.id.ivStarFill)

        tvTitle = findViewById(R.id.tvName)
        tvAddress = findViewById(R.id.tvAddress)
        tvDistance = findViewById(R.id.tvDistance)

        ivAvatar = findViewById(R.id.ivAvatar)

        viewPager = findViewById(R.id.vpSingleObject)

        loadObjectDetails(intent.getIntExtra(IntentHelper.EXTRA_OBJECT_ID, 0))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==ChooseEditFieldActivity.REQUEST_CODE_EDIT_OBJECT||
                    requestCode==EditStateActivity.REQUEST_CODE_EDIT_STATE_OBJECT)
            {
                catalogObject?.let { loadObjectDetails(it.id) }
            }
        }
    }
    //companion object {
       // val EXTRA_OBJECT_KEY = "object"
   // }
    private fun updateUI()
    {
        catalogObject?.let {
            Glide.with(this)
                    .load(it.detailPicture?.fullImagePath())
                    .into(mBinding.ivAvatar)
            Glide.with(this)
                    .load(it.detailPicture?.fullImagePath())
                    .into(object: SimpleTarget<Drawable>(resources.displayMetrics.widthPixels, 100){
                        override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
                            toolbar.contentScrim = resource
                        }
                    })

            ivBackToolbar.setOnClickListener { onBackPressed() }

            tvTitle.text = it.name
            tvDistance.text = "250m"
            tvAddress.text = it.propertyAddress

            tvTitleToolbar.text = it.name

            pagerAdapter = CatalogObjectDetailsPagerAdapter(it, supportFragmentManager)
            viewPager.adapter = pagerAdapter

            tabLayout = findViewById(R.id.tabs)
            tabLayout.setupWithViewPager(viewPager)

            mBinding.fabLink.visibility = if(!catalogObject?.detailPageUrl.isNullOrEmpty()) View.VISIBLE else View.GONE
            mBinding.fabFavourite.visibility = if(Settings.userToken.isNullOrEmpty()) View.GONE else View.VISIBLE

            isFavourite=it.isFavourite!=null && it.isFavourite==true
            updateFavouriteBtn(isFavourite)

            findViewById<AppBarLayout>(R.id.appbar).addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if(appBarLayout!!.totalScrollRange + verticalOffset < 100){
                    tvTitleToolbar.visibility = View.VISIBLE
                    ivStarToolbar.visibility = View.VISIBLE
                } else {
                    tvTitleToolbar.visibility = View.INVISIBLE
                    ivStarToolbar.visibility = View.INVISIBLE
                }
            }
        }
    }
    private fun updateFavouriteBtn(isFavourite:Boolean)
    {
        mBinding.fabFavourite.setImageResource(if(isFavourite) R.drawable.ic_favourite_on else R.drawable.ic_favourite_off)
    }
    private fun showProgress(showProgress: Boolean) {
        mBinding.flLoading.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    private fun loadObjectDetails(objectId:Int)
    {
        val filters = HashMap<String,String>().apply { put("filter[ID][0]", objectId.toString()) }
        filters.put("filter[ID][0]", objectId.toString())
        Settings.catalogFilters?.forEach { filters.put("filter[$it]","Y") }
        compositeDisposable.add(ServiceGenerator.serverApi.getCatalogObjects(HashMap<String,String>().apply { put("filter[ID][0]", objectId.toString()) },
                1, 1, ApiHelper.fullParams)
                .compose(RxHelper.applySchedulers())
                .doFinally {
                    if(catalogObject!=null)
                    {
                        updateUI()
                        showProgress(false)
                    }
                    else finish()
                }
                .subscribeWith(object : DisposableSingleObserver<BaseResponseWithDataObject<CatalogObject>>() {
                    override fun onSuccess(result: BaseResponseWithDataObject<CatalogObject>) {
                        catalogObject=result.data?.items?.firstOrNull()
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (e is UnknownHostException || e is ConnectException)
                            Toast.makeText(applicationContext, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(applicationContext, R.string.server_error, Toast.LENGTH_SHORT).show()
                    }
                }))
    }

    fun onLinkClick(v:View)
    {
        catalogObject?.detailPageUrl?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(catalogObject?.getFullLink())))
        }
    }
    fun onFavouriteClick(v:View)
    {
        catalogObject?.let {
            isFavourite=!isFavourite
            updateFavouriteBtn(isFavourite)
            NetworkIntentService.toogleFavourite(this, it.id)
        }
    }
    fun onEditClick(v:View)
    {
        catalogObject?.let {
            startActivityForResult(Intent(this, ChooseEditFieldActivity::class.java)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, it.id), ChooseEditFieldActivity.REQUEST_CODE_EDIT_OBJECT)
        }
    }
    fun onAlertClick(v:View)
    {
        catalogObject?.let {
            startActivityForResult(Intent(this, EditStateActivity::class.java)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, it.id), EditStateActivity.REQUEST_CODE_EDIT_STATE_OBJECT)
        }
    }
    fun onRouteClick(v:View)
    {
        //TODO
    }
    fun onPhotoClick(v:View)
    {
        //TODO
    }

    //----- Следим за событиями с сервиса --------
    override val actions: Array<String>
        get() = arrayOf(IntentHelper.ACTION_TOGGLE_FAVOURITE)

    override fun onEventReceived(action: String, errorMsg: String?, data: Intent?) {
        if(!errorMsg.isNullOrEmpty()) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            //Ошибка при добавлении в избранное, поэтому инвертируем
            isFavourite=!isFavourite
            updateFavouriteBtn(isFavourite)
        }

    }
}
