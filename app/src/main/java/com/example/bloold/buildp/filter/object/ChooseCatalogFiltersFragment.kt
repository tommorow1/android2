package com.example.bloold.buildp.filter.`object`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import com.example.bloold.buildp.MyApplication
import com.example.bloold.buildp.R
import com.example.bloold.buildp.adapter.FilterAdapter
import com.example.bloold.buildp.common.Settings
import com.example.bloold.buildp.components.OnFilterApplyListener
import com.example.bloold.buildp.model.SortObject

class ChooseCatalogFiltersFragment : android.support.v4.app.Fragment()
{
    private lateinit var rootView: View
    private lateinit var filterAdapter:FilterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_catalog_object_4, container, false)

        val dynamicFilters: SortObject? = arguments?.let { arguments.getParcelable(ChooseCatalogFiltersFragment.ITEMS_KEY) }

        val staticFilters=SortObject(getString(R.string.extra_static_filters), null).apply {
            child=arrayOf(SortObject(MyApplication.instance.getString(R.string.filter_unesco),"UNESCO"),
                SortObject(MyApplication.instance.getString(R.string.filter_vip),"VALUABLE"),
                SortObject(MyApplication.instance.getString(R.string.filter_history),"HISTORIC_SETTLEMENT"),
                SortObject(MyApplication.instance.getString(R.string.filter_quest),"QUEST_ID"),
                SortObject(MyApplication.instance.getString(R.string.filter_vote),"VOTING_ID")
            )}
        val listView = rootView.findViewById<ExpandableListView>(R.id.catalogObjectListView)
        val fullFilter = ArrayList<SortObject>()
        dynamicFilters?.let { fullFilter.add(it) }
        fullFilter.add(staticFilters)
        filterAdapter=FilterAdapter(activity.applicationContext, fullFilter.toTypedArray())
        Settings.catalogFilters?.let { filterAdapter.selectedIds.addAll(it) }
        listView.setAdapter(filterAdapter)

        rootView.findViewById<Button>(R.id.btnViewFind).setOnClickListener {
            Settings.catalogFilters=null
            Settings.catalogFilters=filterAdapter.selectedIds
            if(activity is OnFilterApplyListener)
                (activity as OnFilterApplyListener).onFilterApplied()
        }
        return rootView
    }

    companion object{
        private val ITEMS_KEY = "items"
        fun newInstance(items: SortObject?): ChooseCatalogFiltersFragment
                = ChooseCatalogFiltersFragment().apply { arguments= Bundle() }.apply { arguments.putParcelable(ITEMS_KEY, items) }
    }
}