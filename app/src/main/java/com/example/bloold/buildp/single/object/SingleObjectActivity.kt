package com.example.bloold.buildp.single.`object`

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.PagerClassAdapter
import com.example.bloold.buildp.model.CatalogObjectsModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_object)

        if(savedInstanceState?.containsKey(EXTRA_OBJECT_KEY) ?: false){
            item = savedInstanceState?.getParcelable(EXTRA_OBJECT_KEY)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        pagerAdapter = PagerClassAdapter(supportFragmentManager)
        viewPager = findViewById<ViewPager>(R.id.vpSingleObject)
        viewPager.adapter = pagerAdapter

        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    companion object {
        val EXTRA_OBJECT_KEY = "object"
    }
}
