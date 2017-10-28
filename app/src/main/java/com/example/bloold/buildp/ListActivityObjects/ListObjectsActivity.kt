package com.example.bloold.buildp.ListActivityObjects

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.model.CatalogObjectsModel

import kotlinx.android.synthetic.main.activity_list_objects.*

class ListObjectsActivity : AppCompatActivity() {

    private var objectsArray: ArrayList<CatalogObjectsModel> = ArrayList()

    companion object {
        val KEY_LIST_OBJECT = "list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_objects)
        setSupportActionBar(toolbar)

        objectsArray = intent.getParcelableArrayListExtra<CatalogObjectsModel>(KEY_LIST_OBJECT)

        supportFragmentManager.beginTransaction().add(R.id.container, CatalogObjectFragment.newInstance(objectsArray, true)).commit()
    }
}
