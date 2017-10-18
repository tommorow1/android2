package com.example.bloold.buildp.catalog.`object`

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.CatalogObjectsModel

class CatalogObjectFragment : Fragment(), AdapterListener, CatalogPresenterListener {

    private lateinit var rvCatalog: RecyclerView
    private var adapter: AdapterCatalogObject = AdapterCatalogObject(this)
    private val presenter: CatalogObjectsPresenter = CatalogObjectsPresenter(this)
    private lateinit var urlResponse: String

    companion object Catalog{

        private val KEY_RESPONSE = "response"

        fun newInstance(response: String): CatalogObjectFragment {
            var fragment = CatalogObjectFragment()
            fragment.arguments = Bundle().apply { putString(KEY_RESPONSE, response) }

            return fragment
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

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)

            rvCatalog = view

            rvCatalog.adapter = adapter

            presenter.getCatalogObjects(urlResponse)
        }
    }

    override fun onObjectClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
