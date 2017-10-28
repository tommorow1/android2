package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bloold on 20.10.17.
 */
class DocModel() : Parcelable {
    var name: String? = null
    var code: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        code = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocModel> {
        override fun createFromParcel(parcel: Parcel): DocModel {
            return DocModel(parcel)
        }

        override fun newArray(size: Int): Array<DocModel?> {
            return arrayOfNulls(size)
        }
    }
}