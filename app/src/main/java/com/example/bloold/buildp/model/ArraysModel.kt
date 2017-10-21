package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bloold on 21.10.17.
 */
class HightFilterModelLevel():
        FilterModel<SubFilterModelLevel>(){

    constructor(parcel: Parcel) : this() {
        parcel.readTypedList(items, SubFilterModelLevel.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HightFilterModelLevel> {
        override fun createFromParcel(parcel: Parcel): HightFilterModelLevel {
            return HightFilterModelLevel(parcel)
        }

        override fun newArray(size: Int): Array<HightFilterModelLevel?> {
            return arrayOfNulls(size)
        }
    }
}

class SubFilterModelLevel():
        FilterModel<CatalogObjectsModel>() {

    constructor(parcel: Parcel) : this() {
        parcel.readTypedList(items, CatalogObjectsModel.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubFilterModelLevel> {
        override fun createFromParcel(parcel: Parcel): SubFilterModelLevel {
            return SubFilterModelLevel(parcel)
        }

        override fun newArray(size: Int): Array<SubFilterModelLevel?> {
            return arrayOfNulls(size)
        }
    }
}

abstract class FilterModel<T: Parcelable>(): Parcelable {

    protected val items: ArrayList<T> = ArrayList()

    fun get(pos: Int): T {
        return items.get(pos)
    }

    fun size(): Int {
        return items.size
    }
}