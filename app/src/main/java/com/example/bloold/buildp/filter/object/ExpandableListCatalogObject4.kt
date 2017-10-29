package com.example.bloold.buildp.filter.`object`

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.SortObject

/**
 * Created by mikha on 29-Oct-17.
 */
class ExpandableListCatalogObject4(var mContext: Context?, var mGroups : ArrayList<ArrayList<SortObject>?>) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
       return mGroups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mGroups.get(groupPosition)?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any? {
        return mGroups.get(groupPosition)
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return mGroups.get(groupPosition)?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {

        val convertView2: View?
        convertView2 = if (convertView == null) {
            val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.item_filter_start_root, null)
        } else {
            convertView
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        val textGroup = convertView2?.findViewById<TextView>(R.id.filterRootName)
        textGroup?.text = mGroups?.get(groupPosition)?.get(0)?.name

        return convertView2
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        val convertView2: View?
        convertView2 = if (convertView == null) {
            val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.item_filter_start, null)
        } else {
            convertView
        }

//        val textGroup = convertView2?.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.tvName)
//        textGroup?.text = mGroups?.get(groupPosition)?.get(childPosition)?.name

        val checkBox = convertView2?.findViewById<CheckBox>(R.id.chbFilter)
        checkBox?.text = mGroups?.get(groupPosition)?.get(childPosition)?.name
        checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            Toast.makeText(mContext,"button is pressed",Toast.LENGTH_LONG).show()
        }

        return convertView2
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}