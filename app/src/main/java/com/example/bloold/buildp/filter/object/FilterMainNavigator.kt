package com.example.bloold.buildp.filter.`object`

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.bloold.buildp.R
import com.example.bloold.buildp.model.AllFiltersModel
import com.example.bloold.buildp.model.BaseModel
import com.example.bloold.buildp.model.HightFilterModelLevel
import com.example.bloold.buildp.model.SubFilterModelLevel
import java.util.*
import kotlin.collections.ArrayList

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
        HIGHT_FILTER,
        SUB_LEVEL_FILTER,
        MAIN_FILTER
    }


    private fun showFragment(fragment: Fragment, screen: FilterScreens) {

        if(currentScreen.empty()){
            currentFragment = fragment

            fragmentManager.beginTransaction()
                    .replace(layoutContainerId, fragment)
                    .commit()

            currentScreen.push(screen)

        } else if (currentScreen.peek() != screen) {

            currentFragment = fragment

            fragmentManager.beginTransaction()
                    .replace(layoutContainerId, fragment)
                    .addToBackStack(screen.name)
                    .commit()

            currentScreen.add(screen)

            listener.onScreenNavigate(screen)
        }
    }

    fun onActivityCreate(models: ArrayList<HightFilterModelLevel>) {
        items = models

        currentFragment = initFragment()

        fragmentManager.beginTransaction()
                .add(layoutContainerId, currentFragment)
                .commit()

        listener.onScreenNavigate(FilterScreens.HIGHT_FILTER)
    }

    private fun initFragment(): Fragment {
        currentScreen.add(FilterScreens.HIGHT_FILTER)
        return HighFilterFragment.newInstance(items, R.layout.item_hight_level_filter)
    }

    fun navigateTo(filterScreens: FilterScreens, data: Any? = null){
        when(filterScreens){
            FilterScreens.HIGHT_FILTER -> {
                showFragment(HighFilterFragment.newInstance(items, R.layout.item_hight_level_filter), filterScreens)
            } FilterScreens.SUB_LEVEL_FILTER-> {
                val item = (data as HightFilterModelLevel).items
                if(item != null) {
                    showFragment(SubLevelFragment.newInstance(item, R.layout.item_hight_level_filter), filterScreens)
                }
                Log.d("onListFragmentInte2", item.toString())
            } FilterScreens.MAIN_FILTER -> {
                val item = (data as SubFilterModelLevel).items
                if(item != null) {
                    showFragment(FilterStartFragment.newInstance(item, R.layout.item_filter_start), filterScreens)
                }
            }
        }
    }
}