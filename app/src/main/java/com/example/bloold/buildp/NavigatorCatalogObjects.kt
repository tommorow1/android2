package com.example.bloold.buildp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import java.util.*

/**
 * Created by bloold on 17.10.17.
 */
class NavigatorCatalogObjects(private val activity: AppCompatActivity,
                              private val layoutContainerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    var currentScreen: Stack<CatalogObjectsScreens> = Stack()
    var currentFragment: Fragment? = null

    enum class CatalogObjectsScreens {
        ALL_CATALOG_OBJECTS,
        SINGLE_CATALOG_OBJECTS,
        GROUP_CATALOG_OBJECTS
    }


    private fun showFragment(fragment: Fragment, screen: CatalogObjectsScreens) {
        if (currentScreen.peek() != screen) {

            currentFragment = fragment

            fragmentManager.beginTransaction()
                    .replace(layoutContainerId, fragment)
                    .commit()

            currentScreen.push(screen)
        }
    }

    fun initFragment(){
        //currentFragment = CatalogObjectFragment.newInstance()

        fragmentManager.beginTransaction()
                .replace(layoutContainerId, currentFragment)
                .commit()

        currentScreen.push(CatalogObjectsScreens.ALL_CATALOG_OBJECTS)
    }

    fun navigateTo(catalogObjects: CatalogObjectsScreens, data: Any? = null){
        when(catalogObjects){
            CatalogObjectsScreens.ALL_CATALOG_OBJECTS -> {
                //showFragment(CatalogObjectFragment.newInstance(), catalogObjects)
            }
        }
    }
}