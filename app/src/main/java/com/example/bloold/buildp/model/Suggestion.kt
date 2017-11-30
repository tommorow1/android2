package com.example.bloold.buildp.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by sagus on 30.11.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Suggestion
{
    @get: JsonProperty("ID")
    var id: Int=0
    @get: JsonProperty("OBJECT_ID")
    var objId: Int = 0
    @get: JsonProperty("OBJECT_NAME")
    lateinit var objectName: String
    @get: JsonProperty("STATE_NAME")
    lateinit var stateName: String
    @get: JsonProperty("DATE_CREATE")
    @get: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    lateinit var dateCreate: Date
}