package com.example.bloold.buildp.filter.`object`

import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.FilterModel

class MyFilterStartAdapter<T: Parcelable>(private val mValues: FilterModel<T>,
                              private val customRes: Int)
    :RecyclerView.Adapter<MyFilterStartAdapter<T>.ViewHolder>() {

    public override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(customRes,
                    //R.layout.item_filter_start,
                    parent, false)
        return ViewHolder(view)
    }

    public override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        holder.mItem = mValues.get(position)

    }

    public override fun getItemCount():Int {
        return mValues.size()
    }

    inner class ViewHolder( val mView:View):RecyclerView.ViewHolder(mView) {
        val mCheck:CheckBox?
        val mCheckText:TextView

        var mItem: T? = null

        init{
            mCheck = mView.findViewById(R.id.chbFilter)
            mCheckText = mView.findViewById(R.id.tvName)
        }

    }
}
