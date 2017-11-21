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
    private val KEY_USER_TOKEN = "user_token"

    /*** Фильтры для каталога  */
    var catalogFilters: Set<String>?
        get() = prefs.getStringSet(KEY_CATALOG_FILTERS, HashSet<String>())
        set(value) = prefs.edit().putStringSet(KEY_CATALOG_FILTERS, value).apply()


    /*** Токен авторизации  */
    var userToken: String?
        get() = prefs.getString(KEY_USER_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_USER_TOKEN, userToken).apply()

    private val prefs: SharedPreferences
        get() = MyApplication.instance.getSharedPreferences("buildp", 0)
}
