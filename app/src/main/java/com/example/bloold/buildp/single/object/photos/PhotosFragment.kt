package com.example.bloold.buildp.single.`object`.photos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.bloold.buildp.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PhotosFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PhotosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotosFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var tvDescription: TextView
    private var photoList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            photoList = arguments.getStringArrayList(PHOTOS_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {

    }

    companion object {
        private val PHOTOS_KEY = "photos"

        fun newInstance(photos: ArrayList<String>?): PhotosFragment {
            return PhotosFragment().apply { arguments.putStringArrayList(PHOTOS_KEY, photos) }
        }
    }
}// Required empty public constructor
