package hr.algebra.ivanabilic.nba.api

import com.google.gson.annotations.SerializedName

class Data(@SerializedName("data") val players:List<NbaPlayer>)