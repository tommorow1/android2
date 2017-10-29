package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bloold on 29.10.17.
 */
class SortObject() : Parcelable, BaseModel() {
    var child: ArrayList<SortObject>? = null

    constructor(parcel: Parcel) : this() {
        child = parcel.createTypedArrayList(CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTypedList(child)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SortObject> {
        override fun createFromParcel(parcel: Parcel): SortObject {
            return SortObject(parcel)
        }

        override fun newArray(size: Int): Array<SortObject?> {
            return arrayOfNulls(size)
        }
    }
}