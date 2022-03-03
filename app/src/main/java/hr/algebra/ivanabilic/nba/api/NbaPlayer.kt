package hr.algebra.ivanabilic.nba.api

import com.google.gson.annotations.SerializedName

data class NbaPlayer (
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("position") val position : String
)