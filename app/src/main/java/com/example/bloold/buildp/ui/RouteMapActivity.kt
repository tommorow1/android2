package com.example.bloold.buildp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.bloold.buildp.R
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.ui.fragments.MapObjectListFragment

/**
 * Created by sagus on 20.11.2017.
 */
class RouteMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container,
                    MapObjectListFragment.newInstance()).commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        fun launch(cntx: Activity, latFrom:Double,lngFrom:Double, latTo:Double, lngTo:Double) {
            /*cntx.startActivity(Intent(cntx, RouteMapActivity::class.java)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, objectId))*/
        }
    }
}
