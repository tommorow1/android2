package com.example.bloold.buildp.single.`object`.base.list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.AudioModel
import com.example.bloold.buildp.model.DocModel
import com.example.bloold.buildp.model.VideoModel
import com.example.bloold.buildp.single.`object`.PagerSingleObjectAdapter
import com.example.bloold.buildp.single.`object`.photos.PhotoFragment

class ListFragment : Fragment() {

    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            //view.adapter = MyListRecyclerViewAdapter(DummyContent.ITEMS, mListener)
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            //throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {

    }

    companion object {
        private val ARG_COLUMN_COUNT = "column-count"
        private val ITEMS_KEY = "items"
        private val CHILD_RES_KEY = "child"
        private val TYPE_KEY = "type"

        fun newInstance(childRes: Int,
                        listType: PagerSingleObjectAdapter.LIST_TYPE,
                        items: Any?): ListFragment {
            return ListFragment().apply {
                if(items != null && (items as Array<Any>)?.isNotEmpty() ?: false){
                    when(listType){
                        PagerSingleObjectAdapter.LIST_TYPE.AUDIO -> {
                            arguments.putParcelableArray(ITEMS_KEY, items as Array<AudioModel>)
                        } PagerSingleObjectAdapter.LIST_TYPE.VIDEO -> {
                            arguments.putParcelableArray(ITEMS_KEY, items as Array<VideoModel>)
                        } PagerSingleObjectAdapter.LIST_TYPE.PUBLICATIONS -> {
                            arguments.putParcelableArray(ITEMS_KEY, items as Array<DocModel>)
                        }
                    }

                    arguments.putInt(CHILD_RES_KEY, childRes)
                    arguments.putInt(TYPE_KEY, listType.ordinal)
                }

            }
        }
    }
}
