package com.example.bloold.buildp.single.`object`

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.model.CatalogObjectsModel

/**
 * Created by bloold on 19.10.17.
 */
class PagerSingleObjectAdapter(private val item: CatalogObjectsModel, fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager) {

    companion object {
        private val DESCRIPTION = "Описание"
        private val PHOTO = "Фото"
        private val STATE = "Состояние"
        private val SECURITY = "Охрана"
        private val MATERIALS = "Материалы"
        private val AUDIO = "Аудио"
        private val VIDEO = "Видео"
        private val PUBLICATIONS = "Публикации"

        private val KEY_DESCRIPTION = 0
        private val KEY_PHOTO = 1
        private val KEY_STATE = 2
        private val KEY_SECURITY = 3
        private val KEY_MATERIALS = 4
        private val KEY_AUDIO = 5
        private val KEY_VIDEO = 6
        private val KEY_PUBLICATIONS = 7
    }

    override fun getItem(position: Int): android.support.v4.app.Fragment {
        var urlResponse: String = ""

        when(position){
            KEY_DESCRIPTION-> {
                return DescriptionFragment.newInstance()
            } KEY_PHOTO -> {
                return
            } KEY_STATE -> {
                return
            } KEY_SECURITY -> {
                return
            } KEY_MATERIALS -> {
                return
            } KEY_AUDIO -> {
                return
            } KEY_VIDEO -> {
                return CatalogObjectFragment.newInstance()
            } KEY_PUBLICATIONS -> {
                return
            }
        }

        return CatalogObjectFragment.newInstance(urlResponse)
    }

    override fun getCount(): Int {
        return 8
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            KEY_DESCRIPTION -> {
                return DESCRIPTION
            } KEY_PHOTO -> {
                return PHOTO
            } KEY_STATE -> {
                return STATE
            } KEY_SECURITY -> {
                return SECURITY
            } KEY_MATERIALS -> {
                return MATERIALS
            } KEY_AUDIO -> {
                return AUDIO
            } KEY_VIDEO -> {
                return VIDEO
            } KEY_PUBLICATIONS -> {
                return PUBLICATIONS
            }
        }

        return null
    }
}