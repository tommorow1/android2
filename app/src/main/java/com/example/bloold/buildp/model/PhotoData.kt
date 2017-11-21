package com.example.bloold.buildp.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by bloold on 20.10.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PhotoData() : Parcelable
{
    @get: JsonProperty("NAME")
    var name: String? = null
    @get: JsonProperty("DETAIL_TEXT")
    var detailText: String? = null
    @get: JsonProperty("DETAIL_PICTURE")
    lateinit var  detailPicture: PhotoModel

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        detailText = parcel.readString()
        detailPicture = parcel.readParcelable(PhotoModel::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(detailText)
        parcel.writeParcelable(detailPicture, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoData> {
        override fun createFromParcel(parcel: Parcel): PhotoData {
            return PhotoData(parcel)
        }

        override fun newArray(size: Int): Array<PhotoData?> {
            return arrayOfNulls(size)
        }
    }
}