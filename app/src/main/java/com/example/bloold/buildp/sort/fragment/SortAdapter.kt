package com.example.bloold.buildp.sort.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.BaseModel

interface onFilterClickListener<T> {
    fun onClick(item: T)
}

class SortAdapter<T: BaseModel>(private val mValues: List<T>,
                              private val customRes: Int,
                                         private val listener: onFilterClickListener<T>)
    :RecyclerView.Adapter<SortAdapter<T>.ViewHolder>() {

    public override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(customRes,
                    //R.layout.item_filter_start,
                    parent, false)
        return ViewHolder(view)
    }

    public override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        holder.mItem = mValues.get(position)
        holder.mCheckText.text = mValues.get(position).name

        holder.mView.setOnClickListener( { v ->
            var item = mValues.get(position)
            listener.onClick(item) } )
    }

    public override fun getItemCount():Int {
        return mValues.size
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
