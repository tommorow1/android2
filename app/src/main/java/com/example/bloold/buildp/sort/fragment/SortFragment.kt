package com.example.bloold.buildp.sort.fragment

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
import com.example.bloold.buildp.model.SortObject

class SortFragment : Fragment(), onFilterClickListener<SortObject> {

    private var mListener: OnListFragmentInteractionListener? = null
    private var mItems: ArrayList<SortObject>? = null
    private var mItemRes: Int = 0
    private var adapter: SortAdapter<SortObject>? = null

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

            adapter = SortAdapter(mItems!!, mItemRes, this)

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
            e.printStackTrace()
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
        mItems = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SortObject)
    }

    override fun onClick(item: SortObject) {
        Log.d("onClickFragment", item.name)
        mListener?.onListFragmentInteraction(item)
    }

    companion object{

        private val ITEMS_KEY = "items"
        private val RESOURCE_ITEM_KEY = "container"

        fun newInstance(items: ArrayList<SortObject>, itemRes: Int): SortFragment {
            val fragment = SortFragment()
            val args = Bundle()
            args.putParcelableArrayList(ITEMS_KEY, items)
            args.putInt(RESOURCE_ITEM_KEY, itemRes)
            fragment.arguments = args
            return fragment
        }
    }
}
