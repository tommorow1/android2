package com.example.bloold.buildp.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.bloold.buildp.R
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.databinding.ActivityImageBinding


/**
 * Created by Leonov Oleg, http://pandorika-it.com on 25.05.16.
 */
class ImageViewActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityImageBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Glide.with(this)
                .load(intent.getStringExtra(IntentHelper.EXTRA_IMAGE_URL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.imageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
