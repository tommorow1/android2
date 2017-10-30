package com.example.bloold.buildp.catalog.`object`

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloold.buildp.R
import com.example.bloold.buildp.callback
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.HightFilterModelLevel
import com.example.bloold.buildp.model.PhotoModel
import com.example.bloold.buildp.model.SortObject
import com.example.bloold.buildp.single.`object`.SingleObjectActivity
import kotlinx.android.synthetic.main.fragment_show_objects.*

class CatalogObjectFragment : Fragment(), AdapterListener, callback {

    private lateinit var rvCatalog: RecyclerView
    private var adapter: AdapterCatalogObject = AdapterCatalogObject(this)
    private val presenter: CatalogObjectsPresenter = CatalogObjectsPresenter(this)
    private var urlResponse: String? = null
    private var objectsArray: ArrayList<CatalogObjectsModel> = ArrayList()
    private var isHaveCatalog: Boolean = false
    var sortedObject: SortObject? = null

    companion object Catalog{

        private val KEY_RESPONSE = "response"
        private val KEY_RESPONSE_ARRAY_OBJECTS = "catalog_response"
        private val KEY_RESPONSE_HAVE_CATALOG = "catalog"
        private val KEY_RESPONSE_SORTED_OBJECTS = "sorted"

        fun newInstance(response: String): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply { putString(KEY_RESPONSE, response) } }
        }

        fun newInstance(items: ArrayList<String>): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply { putStringArrayList(KEY_RESPONSE, items) } }
        }

        fun newInstance(items: ArrayList<CatalogObjectsModel>, isHave: Boolean = true): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply { putParcelableArrayList(KEY_RESPONSE_ARRAY_OBJECTS, items)
                    putBoolean(KEY_RESPONSE_HAVE_CATALOG, isHave)} }
        }

        fun newInstance(response: String, sortObject: SortObject): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply {
                        putString(KEY_RESPONSE, response)
                        putParcelable(KEY_RESPONSE_SORTED_OBJECTS, sortObject)
                    } }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null){
            if(arguments.containsKey(KEY_RESPONSE))
                urlResponse = arguments.getString(KEY_RESPONSE)
            else if (arguments.containsKey(KEY_RESPONSE_ARRAY_OBJECTS)) {
                objectsArray = arguments.getParcelableArrayList(KEY_RESPONSE_ARRAY_OBJECTS)
                isHaveCatalog = arguments.getBoolean(KEY_RESPONSE_HAVE_CATALOG)
            }
            if(arguments.containsKey(KEY_RESPONSE_SORTED_OBJECTS)){
                sortedObject = arguments.getParcelable(KEY_RESPONSE_SORTED_OBJECTS)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_catalog_object, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)

            rvCatalog = view

            rvCatalog.adapter = adapter
            Log.d("presenter", urlResponse.toString() + isHaveCatalog.toString())
            if(!isHaveCatalog) {
                if(urlResponse != null) {
                    Log.d("presenter", urlResponse)
                    presenter.getCatalogObjects(urlResponse!!)
                }
            } else {
                onObjectsLoaded(objectsArray)
            }
            if(sortedObject != null){

            }
        }
    }

    override fun onObjectClicked(item: CatalogObjectsModel) {
        val intent = Intent(context, SingleObjectActivity::class.java)
        intent.putExtras(Bundle().apply { putParcelable(SingleObjectActivity.EXTRA_OBJECT_KEY, item) })
        startActivity(intent)
    }

    override fun onObjectsLoaded(items: ArrayList<CatalogObjectsModel>) {
        adapter.addAll(items)
    }

    override fun onFiltersLoaded(items: ArrayList<HightFilterModelLevel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
