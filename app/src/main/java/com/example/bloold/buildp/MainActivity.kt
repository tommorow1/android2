package com.example.bloold.buildp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.bloold.buildp.filter.`object`.FilterObjectsActivity
import com.example.bloold.buildp.search.SearchActivity
import android.content.SharedPreferences
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.bloold.buildp.sort.fragment.FilterMainNavigator
import com.example.bloold.buildp.sort.fragment.onFilterListener
import com.example.bloold.buildp.model.*
import com.example.bloold.buildp.presenters.SortPresenter
import com.example.bloold.buildp.presenters.callback
import com.example.bloold.buildp.sort.fragment.SortFragment
import de.hdodenhof.circleimageview.CircleImageView
import com.facebook.login.LoginManager;



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        callback,
        onFilterListener,
        SortFragment.OnListFragmentInteractionListener{


    private val AuthToken = "AuthToken"
    var navigationView: NavigationView? = null
    //private lateinit var navigator: NavigatorCatalogObjects
    /*private lateinit var pagerAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
*/
    private val BASE_URL = "http://ruinnet.idefa.ru/api_app"
    private val CATALOG_OBJECTS_URL = "/object/index/"
    private val CATALOG_OBJECTS_SELECT = "?select[]=ID&select[]=NAME"
    private val CATALOG_OBJECTS_FILTER = "&filter[IBLOCK_SECTION_ID][1]="
    private val URL = "http://ruinnet.idefa.ru/api_app/directory/type-catalog-structure/"
    private var presenter: SortPresenter = SortPresenter(this)
    private var navigator: FilterMainNavigator? = null

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
/*

        pagerAdapter = PagerClassAdapter(supportFragmentManager)
        viewPager = findViewById<ViewPager>(R.id.container)
        viewPager.adapter = pagerAdapter

        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
*/


        val sPref = getSharedPreferences("main", Context.MODE_PRIVATE) as SharedPreferences
        val AuthTokenSuccess = sPref.getString(AuthToken, "")
        if (!sPref.contains(AuthToken)) {
            hideItem();
        }else{
            //LogUserInfo();

        }
        drawer.openDrawer(GravityCompat.START)

        navigator = FilterMainNavigator(this, R.id.mainContainer, this)
        presenter.execute(URL)
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

    override fun onObjectsLoaded(items: ArrayList<SortObject>) {

    }

    override fun onSortedObjectsLoaded(items: ArrayList<SortObject>) {
        navigator?.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, items)
    }

    override fun onScreenNavigate(screen: FilterMainNavigator.FilterScreens) {

    }

    override fun onListFragmentInteraction(item: SortObject) {
        Log.d("mainListener", item.child.toString())
        if(item.child != null) {
            navigator?.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, item.child)
        } else {
            navigator?.navigateTo(FilterMainNavigator.FilterScreens.CATALOG_OBJECTS, BASE_URL + CATALOG_OBJECTS_URL + CATALOG_OBJECTS_SELECT + CATALOG_OBJECTS_FILTER + item.id)
        }
    }
}
