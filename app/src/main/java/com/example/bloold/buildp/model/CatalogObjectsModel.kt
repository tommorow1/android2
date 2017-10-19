package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bloold on 18.10.17.
 */
class CatalogObjectsModel() : Parcelable {
    var name: String? = null
    var id: Int = 0
    var property_address: String? = null
    var preview_text: String? = null
    var src: String? = null
    var photos: ArrayList<String>? = null
    var docs: ArrayList<String>? = null
    var publications: ArrayList<String>? = null
    var videos: ArrayList<String>? = null
    var audios: ArrayList<String>? = null
    var isFavorite: Boolean? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        id = parcel.readInt()
        property_address = parcel.readString()
        preview_text = parcel.readString()
        src = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(id)
        parcel.writeString(property_address)
        parcel.writeString(preview_text)
        parcel.writeString(src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatalogObjectsModel> {
        override fun createFromParcel(parcel: Parcel): CatalogObjectsModel {
            return CatalogObjectsModel(parcel)
        }

        override fun newArray(size: Int): Array<CatalogObjectsModel?> {
            return arrayOfNulls(size)
        }
    }
}