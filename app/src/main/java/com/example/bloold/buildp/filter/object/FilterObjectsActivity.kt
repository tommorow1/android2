package com.example.bloold.buildp.filter.`object`

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.bloold.buildp.R

class FilterObjectsActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvClear: TextView
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_objects)

        tvTitle = findViewById(R.id.tvTitle)
        tvClear = findViewById(R.id.tvEndClear)
        ivBack = findViewById(R.id.ivBack)
    }
}
