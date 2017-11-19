package com.example.bloold.buildp.common

import android.content.SharedPreferences
import android.text.TextUtils

import com.example.bloold.buildp.MyApplication

import java.util.HashSet

/**
 * Created by Leonov Oleg, http://pandorika-it.com
 */

object Settings {
    private val KEY_CATALOG_FILTERS = "catalogFilters"

    /*** Фильтры для каталога  */
    var catalogFilters: Set<String>?
        get() = prefs.getStringSet(KEY_CATALOG_FILTERS, HashSet<String>())
        set(value) { prefs.edit().putStringSet(KEY_CATALOG_FILTERS, value).apply() }

    private val prefs: SharedPreferences
        get() = MyApplication.instance.getSharedPreferences("buildp", 0)
}
