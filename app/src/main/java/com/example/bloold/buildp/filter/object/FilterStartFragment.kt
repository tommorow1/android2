package com.example.bloold.buildp.filter.`object`

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.BaseModel
import com.example.bloold.buildp.model.FilterModel
import com.example.bloold.buildp.model.HightFilterModelLevel

class FilterStartFragment: Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private var mItems: ArrayList<BaseModel> = ArrayList()
    private var mItemRes: Int = 0
    private var adapter: MyFilterStartAdapter<BaseModel>? = null


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

            adapter = MyFilterStartAdapter(mItems, mItemRes)

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
        fun onListFragmentInteraction()
    }

    companion object{

        private val ITEMS_KEY = "items"
        private val RESOURCE_ITEM_KEY = "container"

        fun <T: Parcelable> newInstance(items: ArrayList<T>, itemRes: Int): FilterStartFragment {
            val fragment = FilterStartFragment()
            val args = Bundle()
            args.putParcelableArrayList(ITEMS_KEY, items)
            args.putInt(RESOURCE_ITEM_KEY, itemRes)
            fragment.arguments = args
            return fragment
        }
    }
}
