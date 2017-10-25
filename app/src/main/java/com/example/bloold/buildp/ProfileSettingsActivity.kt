package com.example.bloold.buildp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View


class ProfileSettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_settings)
        setSupportActionBar(findViewById<View>(R.id.toolbar_actionbar) as Toolbar?)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.title = "Профиль  пользователя"
        }
    }
}