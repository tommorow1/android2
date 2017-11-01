package com.example.bloold.buildp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.bloold.buildp.search.SearchActivity
import android.content.SharedPreferences
import android.support.v7.widget.AppCompatButton
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bloold.buildp.map.BigClusteringDemoActivity
import com.example.bloold.buildp.sort.fragment.FilterMainNavigator
import com.example.bloold.buildp.sort.fragment.onFilterListener
import com.example.bloold.buildp.model.*
import com.example.bloold.buildp.presenters.SortPresenter
import com.example.bloold.buildp.presenters.callback
import com.example.bloold.buildp.sort.fragment.SortFragment
import de.hdodenhof.circleimageview.CircleImageView
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        callback,
        onFilterListener,
        SortFragment.OnListFragmentInteractionListener
{

    private val AuthToken = "AuthToken"
    var navigationView: NavigationView? = null
    //private lateinit var navigator: NavigatorCatalogObjects
    /*private lateinit var pagerAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

*/
    private val BASE_URL = "http://ruinnet.idefa.ru/api_app"
    private val CATALOG_OBJECTS_URL = "/object/list/"
    private val CATALOG_OBJECTS_SELECT = "?select[]=ID&select[]=NAME&select[]=DETAIL_PICTURE&select[]=PREVIEW_TEXT&select[]=PROPERTY_ADDRESS&select[]=PHOTOS_DATA&select[]=IS_FAVORITE&filter[INCLUDE_SUBSECTIONS]=Y"
    private val CATALOG_OBJECTS_FILTER = "&filter[IBLOCK_SECTION_ID][1]="
    private val URL = "http://ruinnet.idefa.ru/api_app/directory/type-catalog-structure/"
    private val CATALOG_ALL_OBJECT = "http://ruinnet.idefa.ru/api_app/object/list/?select[]=ID&select[]=NAME&select[]=PREVIEW_TEXT&select[]=PROPERTY_ADDRESS&select[]=DETAIL_PICTURE&select[]=PHOTOS_DATA&select[]=DOCS_DATA&select[]=PUBLICATIONS_DATA&select[]=VIDEO_DATA&select[]=AUDIO_DATA&select[]=DETAIL_PAGE_URL&select[]=IS_FAVORITE&select[]=PROPERTY_MAP=Y"
    private var presenter: SortPresenter = SortPresenter(this)
    private var navigator: FilterMainNavigator? = null
    private lateinit var ivProfile : CircleImageView
    private lateinit var tvName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        /*val fabAdd = findViewById<View>(R.id.fabAdd) as FloatingActionButton
        fabAdd.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        val fabFilter = findViewById<View>(R.id.fabFilter) as FloatingActionButton
        fabFilter.setOnClickListener { view ->
            //startActivity(Intent(this, FilterObjectsActivity::class.java))
            navigator?.navigateTo(FilterMainNavigator.FilterScreens.FILTER)
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
            LogUserInfo();
            showItem();

        }
       // drawer.openDrawer(GravityCompat.START)

        navigator = FilterMainNavigator(this, R.id.mainContainer, this)
        presenter.execute(URL)
    }

    private fun LogUserInfo(){
        var sPref = getSharedPreferences("main", MODE_PRIVATE)
        var AuthTokenSuccess = sPref.getString(LoginActivity.AuthToken,"")
        var url = "http://ruinnet.idefa.ru/api_app/user/profile/"
        var client = AsyncHttpClient()

        var params = RequestParams()

        client.setBasicAuth("defa","defa")
        client.addHeader("Device-Id","0000")
        client.addHeader("Auth-Token",AuthTokenSuccess)

        var context = this

        client.get(url, params, object : TextHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {

            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                var response: JSONObject? = null
                try {
                    response = JSONObject(responseString)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var image: String? = ""
                var code: String? = ""
                var name: String? = ""
                var last_name: String? = ""

                var data: JSONObject? = null

                try {
                    code = response?.getString("CODE")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if(code.equals("200")) {

                    try {
                        data = JSONObject(response?.getString("DATA"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        name = data?.getString("NAME")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        last_name = data?.getString("LAST_NAME")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    tvName.setText(name+" "+last_name);


                    try {
                        image = data?.getString("PHOTO")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if(image != null && !TextUtils.isEmpty(image) && image != "null")
                        Glide.with(context).load("http://ruinnet.idefa.ru" + image).into(ivProfile)
                }

            }

            override fun onStart() {
                // Initiated the request
            }


            override fun onFinish() {
                // Completed the request (either success or failure)
            }
        })
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
        val tvName = findViewById<View>(R.id.tvName) as TextView;
        val ball = findViewById<View>(R.id.ball) as TextView
        //btnLogout.setWidth(160);
        btnAuth.setWidth(0);



        btnAuth.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.VISIBLE);
        ivProfile.setVisibility(View.VISIBLE);
        ivSettings.setVisibility(View.VISIBLE);
        ball.setVisibility(View.VISIBLE);

    }

    fun LogBtnShow()
    {
        val btnAuth =  findViewById<View>(R.id.btnAuth) as AppCompatButton;
        val ivLogout = findViewById<View>(R.id.ivLogout) as ImageView;
        val ivProfile = findViewById<View>(R.id.ivProfile) as CircleImageView;
        val ivSettings = findViewById<View>(R.id.ivSettings) as ImageView;
        val tvName = findViewById<View>(R.id.tvName) as TextView;
        val ball = findViewById<View>(R.id.ball) as TextView;

        tvName.setText("");

        btnAuth.setVisibility(View.VISIBLE);
        ball.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
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
            navigator?.back()
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
        ivProfile = findViewById(R.id.ivProfile)
        tvName = findViewById(R.id.tvName)
        val btnAuth = findViewById<View>(R.id.btnAuth) as Button

        btnAuth.setOnClickListener { view ->
            startActivity(Intent(this, LoginActivity::class.java))

        }

        val ivLogout = findViewById<View>(R.id.ivLogout) as ImageView

        ivLogout.setOnClickListener { view ->
            hideItem();
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
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_map) {
            startActivity(Intent(this, BigClusteringDemoActivity::class.java))
        } else if (id == R.id.nav_catalog) {
            startActivity(Intent(this, MainActivity::class.java))
        }/* else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }*/
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onObjectsLoaded(items: ArrayList<SortObject>) {

    }

    override fun onSortedObjectsLoaded(items: ArrayList<SortObject>) {
        navigator?.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, getItems(items))
    }

    private fun getItems(items: ArrayList<SortObject>, itemId: String? = null ): ArrayList<SortObject> {
        return items.apply {
            if(!TextUtils.equals(items.get(0).name, "Все"))
                add(0, SortObject().apply {
                    name = "Все"
                    child = items
                    if(itemId == null) {
                        id = "-1"
                    } else {
                        id = itemId
                    }
                })
        }
    }

    override fun onScreenNavigate(screen: FilterMainNavigator.FilterScreens) {
        when(screen){
            FilterMainNavigator.FilterScreens.CATALOG_OBJECTS -> {
                fabFilter.visibility = View.VISIBLE
            } FilterMainNavigator.FilterScreens.MAIN_FILTER -> {
                fabFilter.visibility = View.GONE
            } FilterMainNavigator.FilterScreens.FILTER-> {
                fabFilter.visibility = View.GONE
            }
        }
    }

    override fun onListFragmentInteraction(item: SortObject) {
        Log.d("mainListener", item.child.toString())
        if(item.child != null && item.child!!.get(0).child != null && !TextUtils.equals(item.name, "Все")) {
            navigator?.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, getItems(item.child!!, item.id))
        } else {
            var url = ""
            if(TextUtils.equals(item.id, "-1")){
                url = CATALOG_ALL_OBJECT
            } else {
                url = BASE_URL + CATALOG_OBJECTS_URL + CATALOG_OBJECTS_SELECT + CATALOG_OBJECTS_FILTER + item.id
            }
            Log.d("mainListener", url)
            navigator?.navigateTo(FilterMainNavigator.FilterScreens.CATALOG_OBJECTS, url, item)
        }
    }
}