package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bloold on 20.10.17.
 */
class PhotoModel(): Parcelable {
    var name: String? = null
    var src: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        src = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoModel> {
        override fun createFromParcel(parcel: Parcel): PhotoModel {
            return PhotoModel(parcel)
        }

        override fun newArray(size: Int): Array<PhotoModel?> {
            return arrayOfNulls(size)
        }
    }
}