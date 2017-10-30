package com.example.bloold.buildp.single.`object`

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.bloold.buildp.R
import com.example.bloold.buildp.R.id.toolbar
import com.example.bloold.buildp.catalog.`object`.PagerClassAdapter
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.PhotoModel
import kotlinx.android.synthetic.main.activity_single_object.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SingleObjectActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_object)

        if(intent.hasExtra(EXTRA_OBJECT_KEY) ?: false){
            item = intent.getParcelableExtra<CatalogObjectsModel>(EXTRA_OBJECT_KEY)

            for(i: PhotoModel in item?.photos!!){
                Log.d("photos", i.src)
            }
        }

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

        Glide.with(this).load(item?.src).into(ivAvatar)
        Glide.with(this)
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

    companion object {
        val EXTRA_OBJECT_KEY = "object"
    }
}
