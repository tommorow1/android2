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
import com.example.bloold.buildp.model.PhotoModel
import com.example.bloold.buildp.single.`object`.SingleObjectActivity

class CatalogObjectFragment : Fragment(), AdapterListener, callback {

    private lateinit var rvCatalog: RecyclerView
    private var adapter: AdapterCatalogObject = AdapterCatalogObject(this)
    private val presenter: CatalogObjectsPresenter = CatalogObjectsPresenter(this)
    private var urlResponse: String? = null

    companion object Catalog{

        private val KEY_RESPONSE = "response"

        fun newInstance(response: String): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply { putString(KEY_RESPONSE, response) } }
        }

        fun newInstance(items: ArrayList<String>): CatalogObjectFragment {
            return CatalogObjectFragment()
                    .apply { arguments = Bundle().apply { putStringArrayList(KEY_RESPONSE, items) } }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null){
            urlResponse = arguments.getString(KEY_RESPONSE)
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

            if(urlResponse != null) {
                rvCatalog.adapter = adapter
                presenter.getCatalogObjects(urlResponse!!)
            } else {
                
            }
        }
    }

    override fun onObjectClicked(item: CatalogObjectsModel) {
        val intent = Intent(context, SingleObjectActivity::class.java)
        intent.putExtras(Bundle().apply { putParcelable(SingleObjectActivity.EXTRA_OBJECT_KEY, item) })
        startActivity(intent)
    }

    override fun onObjectsLoaded(items: List<CatalogObjectsModel>) {
        adapter.addAll(items)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
