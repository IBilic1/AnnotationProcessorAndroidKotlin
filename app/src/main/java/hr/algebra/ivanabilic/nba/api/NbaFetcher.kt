package hr.algebra.ivanabilic.nba.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.ivanabilic.nba.DATA_IMPORTED
import hr.algebra.ivanabilic.nba.NBA_PROVIDER_URI
import hr.algebra.ivanabilic.nba.NbaReceiver
import hr.algebra.ivanabilic.nba.framework.createPictureName
import hr.algebra.ivanabilic.nba.framework.createPictureUrl
import hr.algebra.ivanabilic.nba.framework.sendBroadcast
import hr.algebra.ivanabilic.nba.framework.setBooleanPreference
import hr.algebra.ivanabilic.nba.handler.downloadImageAndStore
import hr.algebra.ivanabilic.nba.model.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NbaFetcher(private val context: Context) {

    private var nbaApi:NbaApi

    init {
        val retrofit=Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nbaApi=retrofit.create(NbaApi::class.java)
    }

    fun fetchPlayers(){
        val request=nbaApi.fetchPlayers()
        request.enqueue(object:Callback<Data>{
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                response.body()?.let {
                    populatePlayers(it)
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }

        })
    }

    private fun populatePlayers(data: Data) {
        GlobalScope.launch {
            data.players.forEach {
                var picturePath = downloadImageAndStore(
                    context,
                    createPictureUrl(it.last_name, it.first_name),
                    createPictureName(it.last_name, it.first_name)
                )

                val values=ContentValues().apply {
                    put(Player::firstname.name,it.first_name)
                    put(Player::lastname.name,it.last_name)
                    put(Player::position.name,it.position)
                    put(Player::picturePath.name,picturePath)
                    put(Player::best.name,false)
                }
                context.contentResolver.insert(NBA_PROVIDER_URI,values)


            }
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<NbaReceiver>()
        }

    }

}