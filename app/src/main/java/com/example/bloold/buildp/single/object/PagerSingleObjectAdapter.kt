package com.example.bloold.buildp.single.`object`

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.bloold.buildp.R
import com.example.bloold.buildp.catalog.`object`.CatalogObjectFragment
import com.example.bloold.buildp.model.CatalogObjectsModel
import com.example.bloold.buildp.single.`object`.base.list.ListFragment
import com.example.bloold.buildp.single.`object`.photos.PhotoFragment

/**
 * Created by bloold on 19.10.17.
 */
class PagerSingleObjectAdapter(private val item: CatalogObjectsModel,
                               fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager) {

    companion object {
        private val DESCRIPTION = "Описание"
        private val PHOTO = "Фото"
        //private val STATE = "Состояние"
        private val SECURITY = "Охрана"
        private val MATERIALS = "Материалы"
        private val AUDIO = "Аудио"
        private val VIDEO = "Видео"
        private val PUBLICATIONS = "Публикации"

        private val KEY_DESCRIPTION = 0
        private val KEY_PHOTO = 1
        //private val KEY_STATE = 2
        //private val KEY_SECURITY = 2
        //private val KEY_MATERIALS = 2
        private val KEY_AUDIO = 2
        private val KEY_VIDEO = 3
        private val KEY_PUBLICATIONS = 4
    }

    enum class LIST_TYPE {
        SECURITY,
        MATERIALS,
        AUDIO,
        VIDEO,
        PUBLICATIONS
    }

    override fun getItem(position: Int): android.support.v4.app.Fragment? {

        when(position){
            KEY_DESCRIPTION-> {
                return DescriptionFragment.newInstance(item.preview_text)
            } KEY_PHOTO -> {
                return PhotoFragment.newInstance(item.photos)
            } /*KEY_STATE -> {
                return
            } KEY_SECURITY -> {
                return ListFragment.newInstance(R.layout.item_publications, LIST_TYPE.SECURITY, item.)
            } KEY_MATERIALS -> {
                return ListFragment.newInstance(R.layout.item_publications, LIST_TYPE.MATERIALS, item.publications)
            }*/ KEY_AUDIO -> {
                return ListFragment.newInstance(R.layout.item_publications, LIST_TYPE.MATERIALS, item.publications)
            } KEY_VIDEO -> {
                return ListFragment.newInstance(R.layout.item_publications, LIST_TYPE.VIDEO, item.videos)
            } KEY_PUBLICATIONS -> {
                return ListFragment.newInstance(R.layout.item_publications, LIST_TYPE.MATERIALS, item.publications)
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            KEY_DESCRIPTION -> {
                return DESCRIPTION
            } KEY_PHOTO -> {
                return PHOTO
            } /*KEY_STATE -> {
                return STATE
            }KEY_SECURITY -> {
                return SECURITY
            } KEY_MATERIALS -> {
                return MATERIALS
            }*/ KEY_AUDIO -> {
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