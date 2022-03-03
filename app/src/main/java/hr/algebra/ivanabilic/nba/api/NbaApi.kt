package hr.algebra.ivanabilic.nba.api

import retrofit2.Call
import retrofit2.http.GET
const val PICTURE_URL="https://nba-players.herokuapp.com/players"
const val API_URL = "https://www.balldontlie.io/api/v1/players/"
interface NbaApi {
    @GET("?per_page=10")
    fun fetchPlayers() : Call<Data>
}