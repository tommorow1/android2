package com.example.bloold.buildp.single.`object`.photos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.PhotoModel

interface OnListFragmentInteractionListener {
    fun onPhotoClickListener(item: String)
}

class PhotoRecyclerViewAdapter(private val mListener:OnListFragmentInteractionListener?,
                                private val context: Context):
                                RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder>() {

    var mValues: ArrayList<PhotoModel> = ArrayList()

    companion object {
     private val TAG = "PhotoRecyclerViewAdapter"
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position:Int) {

        Glide.with(context)
            .load(mValues.get(position).src)
            .into(holder.mIvPhoto)

        holder.mView.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v:View) {
                if (null != mListener) {
                    mListener.onPhotoClickListener(mValues.get(position).name!!)
                }
            }
        })
    }

    override fun getItemCount():Int {
        return mValues.size
    }

    fun replaceAll(items: ArrayList<PhotoModel>) {
        clear()
        addAll(items, 0)
    }

    fun clear() {
        val size = mValues.size
        mValues.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(items: ArrayList<PhotoModel>?, startPosition: Int) {
        if (items == null || items.size == 0) {
            return
        }

        mValues.addAll(startPosition, items)

        notifyDataSetChanged()
    }

    fun addAll(items: ArrayList<PhotoModel>?) {
        addAll(items, mValues.size)
        Log.d("adapter", mValues.size.toString())
    }

    inner class ViewHolder( val mView:View):RecyclerView.ViewHolder(mView) {
        val mIvPhoto: ImageView

        init{
            mIvPhoto = mView.findViewById(R.id.ivPhoto)
        }

        override fun toString():String {
            return super.toString() + "${TAG}, ${mValues.size}"
        }
    }
    }
