package com.example.bloold.buildp.model

import android.os.Parcelable

/**
 * Created by bloold on 29.10.17.
 */
class SortObject: BaseModel(), Parcelable {
    var child: List<SortObject>? = null
}