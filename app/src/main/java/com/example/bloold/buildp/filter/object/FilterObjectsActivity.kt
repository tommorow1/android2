package com.example.bloold.buildp.filter.`object`

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.bloold.buildp.HighObjectsFilterResponse
import com.example.bloold.buildp.R
import com.example.bloold.buildp.callback
import com.example.bloold.buildp.model.*
import com.example.bloold.buildp.sort.fragment.FilterMainNavigator
import com.example.bloold.buildp.sort.fragment.SortFragment
import com.example.bloold.buildp.sort.fragment.onFilterListener

class FilterObjectsActivity : AppCompatActivity(),
        callback,
        onFilterListener,
        SortFragment.OnListFragmentInteractionListener{
    override fun onListFragmentInteraction(item: SortObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var tvTitle: TextView
    private lateinit var tvClear: TextView
    private lateinit var ivBack: ImageView

    private val URL = "http://ruinnet.idefa.ru/api_app/directory/type-catalog-structure/"
    private var presenter: HighObjectsFilterResponse = HighObjectsFilterResponse(this)
    private var navigator: FilterMainNavigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_objects)

        tvTitle = findViewById(R.id.tvTitle)
        tvClear = findViewById(R.id.tvEndClear)
        ivBack = findViewById(R.id.ivBack)

        navigator = FilterMainNavigator(this, R.id.flContainerFilter, this)
        //presenter.execute(URL)

        supportFragmentManager.beginTransaction().add(R.id.flContainerFilter, ChooseCatalogFiltersFragment(), "CATALOG_OBJECT_4_FRAGMENT").commit()
    }

    override fun onObjectsLoaded(items: ArrayList<CatalogObjectsModel>) {

    }

    override fun onFiltersLoaded(items: ArrayList<HightFilterModelLevel>) {
        //navigator?.onActivityCreate(items)
    }

    override fun onScreenNavigate(screen: FilterMainNavigator.FilterScreens) {

    }

}
