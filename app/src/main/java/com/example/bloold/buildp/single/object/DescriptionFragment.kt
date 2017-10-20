package com.example.bloold.buildp.single.`object`

import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.bloold.buildp.R

class DescriptionFragment : Fragment() {

    private var mDescription: String? = null
    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mDescription = arguments.getString(DESCRIPTION_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_description, container, false)

        Log.d("onCreate", "description")

        tvDescription = view.findViewById(R.id.tvDescription)
        tvDescription.text = mDescription

        return view
    }

    companion object {
        private val DESCRIPTION_KEY = "description"

        fun newInstance(description: String?): DescriptionFragment {
            return DescriptionFragment().apply {
                if(!description.isNullOrEmpty()){
                    arguments = Bundle().apply { putString(DESCRIPTION_KEY, description)} }
            }
        }
    }
}
