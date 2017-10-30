package com.example.bloold.buildp.single.`object`.base.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bloold.buildp.single.`object`.base.list.ListFragment.OnListFragmentInteractionListener

class MyListRecyclerViewAdapter(private val childRes: Int,
                                private val mValues: List<Any>,
                                private val mListener: OnListFragmentInteractionListener?):
                                RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(childRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mView.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.mItem)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {

        }
    }
}
