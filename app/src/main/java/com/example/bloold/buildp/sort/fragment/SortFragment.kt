package com.example.bloold.buildp.sort.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloold.buildp.R
import com.example.bloold.buildp.components.OnItemClickListener
import com.example.bloold.buildp.model.Category
import com.example.bloold.buildp.ui.MainActivity

class SortFragment : Fragment(), OnItemClickListener<Category> {
    private var adapter: SortAdapter? = null
    var isMain:Boolean=false
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_filter_start_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isMain=arguments?.getBoolean(KEY_IS_MAIN, false)==true

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()

            adapter = SortAdapter(arguments.getParcelableArrayList(ITEMS_KEY)!!, R.layout.item_hight_level_filter, this)

            view.layoutManager = LinearLayoutManager(context)
            view.adapter = adapter
        }
    }


    /*override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as OnListFragmentInteractionListener
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/

    /*override fun onDetach() {
        super.onDetach()
        mListener = null
        mItems = null
    }*/

    /*interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Category)
    }*/

    override fun onItemClick(item: Category) {
        (activity as MainActivity).showCategoryListFragment(item)
        //mListener?.onListFragmentInteraction(item)
    }

    companion object{

        private val ITEMS_KEY = "items"
        private val KEY_IS_MAIN = "main"
        private val RESOURCE_ITEM_KEY = "container"

        fun newInstance(items: ArrayList<Category>, isMain:Boolean): SortFragment {
            val fragment = SortFragment()
            val args = Bundle()
            args.putParcelableArrayList(ITEMS_KEY, items)
            args.putBoolean(KEY_IS_MAIN, isMain)
            fragment.arguments = args
            return fragment
        }
    }
}
