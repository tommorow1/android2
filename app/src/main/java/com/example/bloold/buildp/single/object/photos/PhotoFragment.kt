package com.example.bloold.buildp.single.`object`.photos

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.PhotoModel
import com.example.bloold.buildp.single.`object`.SingleObjectActivity

class PhotoFragment : Fragment(), OnListFragmentInteractionListener{
    private var mColumnCount = 3
    private var mListener: OnListFragmentInteractionListener? = null
    private var photoList: ArrayList<PhotoModel>? = null
    private lateinit var adapter: PhotoRecyclerViewAdapter
    private lateinit var rvPhotos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mColumnCount = 3

        if (arguments.containsKey(PHOTOS_KEY)) {
            photoList = arguments.getParcelableArrayList<PhotoModel>(PHOTOS_KEY)
            for(i: PhotoModel in photoList!!){
                Log.d("photosFragment", i.src)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_photo_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view is RecyclerView) {
            view.layoutManager = GridLayoutManager(view.context, mColumnCount)

            rvPhotos = view

            adapter = PhotoRecyclerViewAdapter(this, view.context)
            adapter.addAll(photoList)

            rvPhotos.adapter = adapter

            for(i: PhotoModel in photoList!!){
                Log.d("photosOnCreate", i.src)
            }
        }
    }

    override fun onPhotoClickListener(item: String) {

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as OnListFragmentInteractionListener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {

        private val ARG_COLUMN_COUNT = "column-count"
        private val PHOTOS_KEY = "photos"

        fun newInstance(photos: ArrayList<PhotoModel>?): PhotoFragment {
            return PhotoFragment().apply {
                arguments = Bundle().apply { putParcelableArrayList(PHOTOS_KEY, photos) }

                for(i: PhotoModel in photos!!){
                    Log.d("photosBundle", i.src)
                }
            }
        }
    }
}
