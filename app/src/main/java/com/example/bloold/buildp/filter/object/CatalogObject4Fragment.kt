package com.example.bloold.buildp.filter.`object`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.model.SortObject
import com.example.bloold.buildp.sort.fragment.SortFragment
import kotlinx.android.synthetic.main.fragment_catalog_object_4.*

/**
 * Created by mikha on 29-Oct-17.
 */
class CatalogObject4Fragment : android.support.v4.app.Fragment() {

    private var rootView: View? = null
    private var mItems: SortObject = SortObject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_catalog_object_4, container, false)

        if (arguments != null) {
            mItems = arguments.getParcelable(CatalogObject4Fragment.ITEMS_KEY)
        }

        val catalogObjects = ArrayList<ArrayList<SortObject>?>()
        val children1 = ArrayList<String>()

        children1.add("Child_1")
        children1.add("Child_2")
        catalogObjects.add(mItems.child)


        val adapter = ExpandableListCatalogObject4(activity.applicationContext, catalogObjects)
        val listView = rootView?.findViewById<ExpandableListView>(R.id.catalogObjectListView)
        listView?.setAdapter(adapter)

        return rootView
    }

    companion object{

        private val ITEMS_KEY = "items"

        fun newInstance(items: SortObject?): CatalogObject4Fragment {
            val fragment = CatalogObject4Fragment()
            val args = Bundle()
            args.putParcelable(ITEMS_KEY, items)
            fragment.arguments = args
            return fragment
        }
    }
}