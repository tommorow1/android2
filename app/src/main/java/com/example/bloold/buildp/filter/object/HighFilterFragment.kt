package com.example.bloold.buildp.filter.`object`

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.HightFilterModelLevel
import com.example.bloold.buildp.model.SubFilterModelLevel

/**
 * Created by bloold on 22.10.17.
 */
class HighFilterFragment: Fragment(), onFilterClickListener<HightFilterModelLevel> {

    private var mListener: OnListFragmentInteractionListener? = null
    private var mItems: ArrayList<HightFilterModelLevel> = ArrayList()
    private var mItemRes: Int = 0
    private var adapter: MyFilterStartAdapter<HightFilterModelLevel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mItems = arguments.getParcelableArrayList(ITEMS_KEY)
            mItemRes = arguments.getInt(RESOURCE_ITEM_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_filter_start_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()

            adapter = MyFilterStartAdapter(mItems, mItemRes, this)

            view.layoutManager = LinearLayoutManager(context)
            view.adapter = adapter
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as OnListFragmentInteractionListener
        } catch (e: Exception) {

        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: HightFilterModelLevel)
    }

    override fun onClick(item: HightFilterModelLevel) {
        Log.d("onClickFragment", item.name)
        mListener?.onListFragmentInteraction(item)
    }

    companion object{

        private val ITEMS_KEY = "items"
        private val RESOURCE_ITEM_KEY = "container"

        fun newInstance(items: ArrayList<HightFilterModelLevel>, itemRes: Int): HighFilterFragment {
            val fragment = HighFilterFragment()
            val args = Bundle()
            args.putParcelableArrayList(ITEMS_KEY, items)
            args.putInt(RESOURCE_ITEM_KEY, itemRes)
            fragment.arguments = args
            return fragment
        }
    }
}