package com.example.bloold.buildp.filter.`object`

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloold.buildp.R

/**
 * Created by mikha on 29-Oct-17.
 */
class CatalogObject4Fragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_catalog_object_4, container, false)


        val catalogObjects = ArrayList<ArrayList<String>>()
        val children1 = ArrayList<String>()
        val children2 = ArrayList<String>()

        children1.add("Child_1")
        children1.add("Child_2")
        catalogObjects.add(children1)

        children2.add("Child_1")
        children2.add("Child_2")
        children2.add("Child_3")
        catalogObjects.add(children2)

//        ExpListAdapter adapter = new ExpListAdapter(getApplicationContext(), groups);
//        listView.setAdapter(adapter);

        return rootView
    }
}