package com.example.bloold.buildp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewParent
import com.example.bloold.buildp.catalog.`object`.PagerClassAdapter
import com.example.bloold.buildp.filter.`object`.FilterObjectsActivity
import com.example.bloold.buildp.search.SearchActivity
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.example.bloold.buildp.R.id.logging_menu
import com.example.bloold.buildp.R.id.nav_view
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.bloold.buildp.model.*
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import com.loopj.android.http.*;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.Header;
import com.facebook.login.widget.*;
import com.facebook.login.LoginManager;



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val AuthToken = "AuthToken"
    var navigationView: NavigationView? = null
    private lateinit var navigator: NavigatorCatalogObjects
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fabAdd = findViewById<View>(R.id.fabAdd) as FloatingActionButton
        fabAdd.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val fabFilter = findViewById<View>(R.id.fabFilter) as FloatingActionButton
        fabFilter.setOnClickListener { view ->
            startActivity(Intent(this, FilterObjectsActivity::class.java))
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        pagerAdapter = PagerClassAdapter(supportFragmentManager)
        viewPager = findViewById<ViewPager>(R.id.container)
        viewPager.adapter = pagerAdapter

        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)


        val sPref = getSharedPreferences("main", Context.MODE_PRIVATE) as SharedPreferences
        val AuthTokenSuccess = sPref.getString(AuthToken, "")
        if (!sPref.contains(AuthToken)) {
            hideItem();
        }else{
            //LogUserInfo();

        }
        drawer.openDrawer(GravityCompat.START)
    }

    fun hideItem()
    {
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView;

        val nav_Menu = navigationView!!.getMenu();
        nav_Menu.findItem(R.id.logging_menu).setVisible(false);
    }

    fun showItem()
    {
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView;
        val nav_Menu = navigationView!!.getMenu();
        nav_Menu.findItem(R.id.logging_menu).setVisible(true);
    }


    fun  LogBtnHide()
    {
        val btnAuth =  findViewById<View>(R.id.btnAuth) as AppCompatButton;
        val ivLogout = findViewById<View>(R.id.ivLogout) as ImageView;
        val ivProfile = findViewById<View>(R.id.ivProfile) as CircleImageView;
        val ivSettings = findViewById<View>(R.id.ivSettings) as ImageView;
        //btnLogout.setWidth(160);
        btnAuth.setWidth(0);



        btnAuth.setVisibility(View.INVISIBLE);
        ivLogout.setVisibility(View.VISIBLE);
        ivProfile.setVisibility(View.VISIBLE);
        ivSettings.setVisibility(View.VISIBLE);

    }

    fun LogBtnShow()
    {
        val btnAuth =  findViewById<View>(R.id.btnAuth) as AppCompatButton;
        val ivLogout = findViewById<View>(R.id.ivLogout) as ImageView;
        val ivProfile = findViewById<View>(R.id.ivProfile) as CircleImageView;
        val ivSettings = findViewById<View>(R.id.ivSettings) as ImageView;
        val tvName = findViewById<View>(R.id.tvName) as TextView;


        tvName.setText("");

        btnAuth.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.INVISIBLE);
        ivProfile.setVisibility(View.INVISIBLE);
        ivSettings.setVisibility(View.INVISIBLE);
    }



    fun DeleteToken()
    {
        val sPref =  getSharedPreferences("main", MODE_PRIVATE) as SharedPreferences;
        val editor = sPref!!.edit()
        editor.remove(AuthToken)
        editor.apply()
        LoginManager.getInstance().logOut();
    }


    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val sPref = getSharedPreferences("main", Context.MODE_PRIVATE) as SharedPreferences
        val AuthTokenSuccess = sPref.getString(AuthToken, "")
        if (sPref.contains(AuthToken)) {
            LogBtnHide()
        }

        val btnAuth = findViewById<View>(R.id.btnAuth) as Button

        btnAuth.setOnClickListener { view ->
            startActivity(Intent(this, LoginActivity::class.java))

        }

        val ivLogout = findViewById<View>(R.id.ivLogout) as ImageView

        ivLogout.setOnClickListener { view ->
            DeleteToken();
            startActivity(Intent(this, MainActivity::class.java))
        }

        val ivSettings = findViewById<View>(R.id.ivSettings) as ImageView

        ivSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this, SearchActivity::class.java))
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        /*// Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        */val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
