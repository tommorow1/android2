package com.example.bloold.buildp.api.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by sagus on 18.11.2017.
 */
class DataObject<T> {
    @get: JsonProperty("ITEMS")
    var items:Array<T>?=null
}