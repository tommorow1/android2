package com.example.bloold.buildp.sort.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.filter.`object`.CatalogObject4Fragment
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.model.HightFilterModelLevel
import com.example.bloold.buildp.model.SortObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by bloold on 22.10.17.
 */
interface onFilterListener {
    fun onScreenNavigate(screen: FilterMainNavigator.FilterScreens)
}

class FilterMainNavigator(private val activity: AppCompatActivity,
                          private val layoutContainerId: Int,
                          private val listener: onFilterListener) {

    private lateinit var items: ArrayList<HightFilterModelLevel>

    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    var currentScreen: Stack<FilterScreens> = Stack()
    var currentFragment: Fragment? = null

    enum class FilterScreens {
        MAIN_FILTER,
        CATALOG_OBJECTS,
        FILTER
    }


    private fun showFragment(fragment: Fragment, screen: FilterScreens) {

        if(currentScreen.empty()){
            currentFragment = fragment

            fragmentManager.beginTransaction()
                    .replace(layoutContainerId, fragment)
                    .commit()

            currentScreen.push(screen)

        } else {
            currentFragment = fragment

            fragmentManager.beginTransaction()
                    .replace(layoutContainerId, fragment)
                    .addToBackStack(screen.name)
                    .commit()

            currentScreen.add(screen)
        }

        listener.onScreenNavigate(screen)
    }

    fun navigateTo(filterScreens: FilterScreens, data: Any? = null, sortObject: SortObject? = null){
        when(filterScreens){
            FilterScreens.MAIN_FILTER -> {
                if(data != null) {
                    showFragment(SortFragment.newInstance(data as ArrayList<SortObject>, R.layout.item_hight_level_filter), filterScreens)
                }
            } FilterScreens.CATALOG_OBJECTS -> {
            if(data != null) {
                showFragment(CatalogObjectFragment.newInstance(data as String, sortObject!!), filterScreens)
            }
        } FilterScreens.FILTER -> {
            showFragment(CatalogObject4Fragment(), filterScreens)
        }
        }
    }

    fun back(){
        if(!currentScreen.empty()) {
            currentScreen.pop()
            listener.onScreenNavigate(currentScreen.peek())
        }
    }
}