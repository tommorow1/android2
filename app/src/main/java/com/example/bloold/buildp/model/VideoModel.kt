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
class VideoModel(): Parcelable {
    @get: JsonProperty("NAME")
    var name: String? = null
    @get: JsonProperty("PROPERTY_DOCUMENT_CODE")
    var code: String? = null
    @get: JsonProperty("SRC")
    lateinit var src: String

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(code)
        parcel.writeString(src)
    }

    override fun describeContents(): Int = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        code = parcel.readString()
        src = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<VideoModel> {
        override fun createFromParcel(parcel: Parcel): VideoModel = VideoModel(parcel)

        override fun newArray(size: Int): Array<VideoModel?> = arrayOfNulls(size)
    }
}