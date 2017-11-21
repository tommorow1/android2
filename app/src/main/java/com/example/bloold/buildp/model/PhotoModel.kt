package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable
import com.example.bloold.buildp.api.ServiceGenerator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by bloold on 20.10.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PhotoModel(): Parcelable {
    var name: String? = null
    @get: JsonProperty("SRC")
    var src: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        src = parcel.readString()
    }
    fun fullImagePath() = if(src==null) src else ServiceGenerator.SITE_URL+src

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(src)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PhotoModel> {
        override fun createFromParcel(parcel: Parcel): PhotoModel = PhotoModel(parcel)

        override fun newArray(size: Int): Array<PhotoModel?> = arrayOfNulls(size)
    }
}