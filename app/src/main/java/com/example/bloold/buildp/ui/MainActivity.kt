package com.example.bloold.buildp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.example.bloold.buildp.R
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.CatalogObject
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.common.RxHelper
import com.example.bloold.buildp.common.Settings
import com.example.bloold.buildp.components.NetworkActivity
import com.example.bloold.buildp.components.OnFilterApplyListener
import com.example.bloold.buildp.databinding.ActivityMainBinding
import com.example.bloold.buildp.model.Category
import com.example.bloold.buildp.profile.LoginActivity
import com.example.bloold.buildp.profile.ProfileSettingsActivity
import com.example.bloold.buildp.search.SearchActivity
import com.example.bloold.buildp.sort.fragment.FilterMainNavigator
import com.example.bloold.buildp.sort.fragment.SortFragment
import com.example.bloold.buildp.sort.fragment.onFilterListener
import com.example.bloold.buildp.ui.fragments.CatalogObjectListFragment
import com.example.bloold.buildp.ui.fragments.MapObjectListFragment
import com.example.bloold.buildp.ui.fragments.NotificationsFragment
import com.example.bloold.buildp.ui.fragments.SuggestionTabsFragment
import com.facebook.login.LoginManager
import com.google.firebase.iid.FirebaseInstanceId
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import io.fabric.sdk.android.Fabric
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONException
import org.json.JSONObject
import java.net.ConnectException
import java.net.UnknownHostException

class MainActivity : NetworkActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var mBinding:ActivityMainBinding
    private val AuthToken = "AuthToken"
    var navigationView: NavigationView? = null
    private lateinit var ivProfile : CircleImageView
    private lateinit var tvName : TextView
    private lateinit var allCategories : ArrayList<Category>
    /*** Текущая категория */
    private var currentCategory : Category?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val llHeader = mBinding.navView.getHeaderView(0) as ConstraintLayout
        tvName = llHeader.findViewById<View>(R.id.tvName) as TextView

        Settings.catalogFilters=null//Очищаем предварительно установленые фильтры
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fabFilter = findViewById<View>(R.id.fabFilter) as FloatingActionButton
        fabFilter.setOnClickListener {
            ChooseFiltersActivity.launch(this, currentCategory,
                    ChooseFiltersActivity.REQUEST_CODE_EDIT_FILTERS)
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val sPref = getSharedPreferences("main", Context.MODE_PRIVATE) as SharedPreferences
        if (!sPref.contains(AuthToken)) {
            hideItem()
        }else{
            LogUserInfo()
            showItem()

        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            currentFragment.onResume()//Для обновления информации в фрагменте, который вернули из стека
            /*if (currentFragment is SortFragment) {
                mBinding.appBarLayout?.fabFilter?.hide()
                getSearchView().setQuery(userCatalogQuery, false)
            } else
                showAppBarElevation(true)*/
            if(currentFragment is SortFragment)
                navigationView.setCheckedItem(R.id.nav_catalog)

            if(currentFragment is CatalogObjectListFragment)
                mBinding.appBarIncludeLayout?.fabFilter?.show()
            else
                mBinding.appBarIncludeLayout?.fabFilter?.hide()

            if (currentFragment is MapObjectListFragment||
                    currentFragment is SuggestionTabsFragment) {
                showAppBarElevation(false)
            } else
                showAppBarElevation(true)

            invalidateOptionsMenu()
/*            if (currentFragment is SortFragment||
                    currentFragment is MapObjectListFragment)
                mBinding.appBarIncludeLayout?.fabFilter?.hide()
            else
                mBinding.appBarIncludeLayout?.fabFilter?.show()*/
        }
        loadCategories()
    }

    private fun showProgress(showProgress: Boolean) {
        mBinding.appBarIncludeLayout?.flLoading?.visibility = if (showProgress) View.VISIBLE else View.GONE
    }
    private fun loadCategories()
    {
        compositeDisposable.add(ServiceGenerator.serverApi.getCategories()
                .compose(RxHelper.applySchedulers())
                .doOnSubscribe { showProgress(true) }
                .doFinally { showProgress(false) }
                .subscribeWith(object : DisposableSingleObserver<BaseResponse<Category>>() {
                    override fun onSuccess(result: BaseResponse<Category>) {
                        allCategories=getItems(ArrayList(result.data?.toMutableList()), "-1")
                        //allCategories=ArrayList(result.data?.toList())
                        showCategoryListFragment()
                        //navigator.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, getItems(ArrayList(result.data?.toList())))
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (e is UnknownHostException || e is ConnectException)
                            Toast.makeText(applicationContext, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(applicationContext, R.string.server_error, Toast.LENGTH_SHORT).show()
                    }
                }))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==ChooseFiltersActivity.REQUEST_CODE_EDIT_FILTERS)
            {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
                (currentFragment as? CatalogObjectListFragment)?.refreshCatalogList()
            }
            else if(requestCode == SearchActivity.REQUEST_CODE_SEARCH)
            {
                showObjectList(currentCategory,
                        data?.getStringExtra(IntentHelper.EXTRA_QUERY_TYPE),
                        data?.getStringExtra(IntentHelper.EXTRA_QUERY_STRING))
            }
        }
    }

    private fun LogUserInfo(){
        var sPref = getSharedPreferences("main", MODE_PRIVATE)
        var AuthTokenSuccess = sPref.getString(LoginActivity.AuthToken,"")
        var url = "http://ruinnet.idefa.ru/api_app/user/profile/"
        var client = AsyncHttpClient()

        var params = RequestParams()

        client.setBasicAuth("defa","defa")
        client.addHeader("Device-Id",Settings.getUdid())
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

            val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            if (currentFragment != null && (currentFragment as? SortFragment)?.isMain==true) {
                finish()
                //super.onBackPressed();
            } else if (supportFragmentManager.backStackEntryCount > 0) {
                super.onBackPressed()
            }
            else
                showCategoryListFragment()

            //navigator.back()
            //super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if(currentFragment !is SuggestionTabsFragment)
            menuInflater.inflate(R.menu.main, menu)


        val sPref = getSharedPreferences("main", Context.MODE_PRIVATE) as SharedPreferences
        if (sPref.contains(AuthToken)) {
            LogBtnHide()
        }
        ivProfile = findViewById<View>(R.id.ivProfile) as CircleImageView
        //tvName = findViewById<View>(R.id.tvName) as TextView
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
            startActivityForResult(Intent(this, SearchActivity::class.java)
                    .putExtra("fromActivity", "catalog"), SearchActivity.REQUEST_CODE_SEARCH)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }

    }

    private fun showAppBarElevation(show: Boolean) {
        //Сделано 0.1 вместо 0, а иначе возврат по кнопке назад в тулбаре не работает
        ViewCompat.setElevation(mBinding.appBarIncludeLayout?.appBarLayout, if (!show) 0.1f else TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics))
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragmentClass: Class<out Fragment>? = null
        val id = item.itemId
        if (id == R.id.nav_map) {
            showMap(null)
        } else if (id == R.id.nav_catalog) {
            showAppBarElevation(true)
            fabFilter.show()
            showCategoryListFragment()
        } else if (id == R.id.nav_my_suggestions) {
            showAppBarElevation(false)
            fabFilter.hide()
            fragmentClass=SuggestionTabsFragment::class.java
        }else if (id == R.id.nav_notice) {
            showAppBarElevation(true)
            fabFilter.hide()
            fragmentClass=NotificationsFragment::class.java
        }

        fragmentClass?.newInstance()?.let { supportFragmentManager.beginTransaction().replace(R.id.mainContainer, it)
                .addToBackStack(null).commit() }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /*override fun onObjectsLoaded(items: ArrayList<SortObject>) {

    }

    override fun onSortedObjectsLoaded(items: ArrayList<SortObject>) {
        navigator.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, getItems(items))
    }*/

    private fun getItems(items: ArrayList<Category>, itemId: String? = null ): ArrayList<Category> {
        return items.apply {
            if(!TextUtils.equals(items[0].name, "Все"))
                add(0, Category().apply {
                    name = "Все"
                    children = items.toTypedArray()
                    id = itemId ?: "-1"
                })
        }
    }

    /*override fun onFilterApplied() {
        navigator.navigateTo(FilterMainNavigator.FilterScreens.CATALOG_OBJECTS)
    }*/

    /*override fun onScreenNavigate(screen: FilterMainNavigator.FilterScreens) {
        when(screen){
            FilterMainNavigator.FilterScreens.CATALOG_OBJECTS -> {
                fabFilter.visibility = View.VISIBLE
            } *//*FilterMainNavigator.FilterScreens.MAIN_FILTER -> {
                fabFilter.visibility = View.GONE
            }*//*
        }
    }*/

    fun showCategoryListFragment(parentCategory: Category)
    {
        currentCategory=parentCategory
        if(parentCategory.name!="Все"&&parentCategory.children!=null) {
            mBinding.appBarIncludeLayout?.fabFilter?.hide()
            supportFragmentManager.beginTransaction().replace(R.id.mainContainer,
                    SortFragment.newInstance(getItems(ArrayList(parentCategory.children?.toMutableList()), parentCategory.id), false))
                    .addToBackStack(null)
                    .commit()
        }
        else
        {
            showObjectList(parentCategory)
        }
    }
    private fun showObjectList(category: Category?, typeQuery:String?=null, stringQuery:String?=null)
    {
        mBinding.appBarIncludeLayout?.fabFilter?.show()
        supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                CatalogObjectListFragment.newInstance(if(category==null||category.id=="-1") null else category, typeQuery, stringQuery))
                .addToBackStack(null)
                .commit()
    }
    fun showCategoryListFragment(categoryList: ArrayList<Category>?=null)
    {
        mBinding.appBarIncludeLayout?.fabFilter?.hide()
        /*val categoryToShow = Category(getString(R.string.all))
                .apply { children=categoryList?.toTypedArray() }*/
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer,
                SortFragment.newInstance(getItems(categoryList?:allCategories), categoryList==null))
                .commit()
    }

    fun showMap(obj:CatalogObject?=null)
    {
        showAppBarElevation(false)
        fabFilter.hide()
        MapObjectListFragment.newInstance(obj).let { supportFragmentManager.beginTransaction().replace(R.id.mainContainer, it)
                .addToBackStack(null).commit() }
    }

    /*override fun onListFragmentInteraction(item: Category) {
        currentCategory=item
        showCategoryListFragment(item)
        //Log.d("mainListener", item.children.toString())
        if(item.children != null && item.children!![0].children != null && !TextUtils.equals(item.name, "Все")) {
            //navigator.navigateTo(FilterMainNavigator.FilterScreens.MAIN_FILTER, getItems(ArrayList(item.children?.toMutableList()), item.id))
        } else {
            var url = if(TextUtils.equals(item.id, "-1")){
                CATALOG_ALL_OBJECT
            } else {
                BASE_URL + CATALOG_OBJECTS_URL + CATALOG_OBJECTS_SELECT + CATALOG_OBJECTS_FILTER + item.id
            }
            //Log.d("mainListener", url)
            navigator.navigateTo(FilterMainNavigator.FilterScreens.CATALOG_OBJECTS, url, item)
        }
    }*/
}