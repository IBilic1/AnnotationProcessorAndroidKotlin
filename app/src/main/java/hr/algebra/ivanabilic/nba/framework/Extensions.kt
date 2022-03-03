package hr.algebra.ivanabilic.nba.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import hr.algebra.ivanabilic.nba.NBA_PROVIDER_URI
import hr.algebra.ivanabilic.nba.NbaService
import hr.algebra.ivanabilic.nba.api.PICTURE_URL
import hr.algebra.ivanabilic.nba.model.Player

fun View.startAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun <reified T :Activity> Context.startActivity()
=startActivity(Intent(this,T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
})

inline fun <reified T :Activity> Context.startActivity(key:String,value:Int)
        =startActivity(Intent(this,T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra(key,value)
})

fun Context.getBooleanPreference(key:String)
=PreferenceManager.getDefaultSharedPreferences(this)
    .getBoolean(key,false)

fun Context.setBooleanPreference(key:String,value:Boolean)
    =PreferenceManager.getDefaultSharedPreferences(this)
    .edit()
    .putBoolean(key,value)
    .apply()

inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
        = sendBroadcast(Intent(this, T::class.java))

inline fun<reified T: Worker> WorkManager.enqueue()
        =enqueue(OneTimeWorkRequestBuilder<NbaService>().build())

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, function: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        function,
        delay
    )
}

fun createPictureUrl(lastname:String,firstname:String)
= "$PICTURE_URL/$lastname/$firstname"

fun createPictureName(lastname:String,firstname:String)
        = "${lastname}_$firstname"

fun Context.fetchPlayers() :MutableList<Player>{
    val players= mutableListOf<Player>()
    val cursor=contentResolver.query(NBA_PROVIDER_URI,
    null,null,null,null)

    while (cursor!=null && cursor.moveToNext()){
        players.add(
            Player(
                cursor.getLong(cursor.getColumnIndexOrThrow(Player::_id.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Player::firstname.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Player::lastname.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Player::picturePath.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Player::position.name)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Player::best.name))==1
            )
        )
    }
    return players
}

inline fun <reified T> Context.getGeneratedImplementation(suffix: String) : T {
    val fullPackage: String = T::class.java.getPackage().getName()
    val name: String =  T::class.java.getCanonicalName()
    val postPackageName =
        if (fullPackage.isEmpty()) name else name.substring(fullPackage.length + 1)
    val implName = postPackageName.replace('.', '_') + suffix
    val fullClassName = if (fullPackage.isEmpty()) implName else "$fullPackage.$implName"
    val aClass = Class.forName(
        fullClassName, true,  T::class.java.getClassLoader()) as Class<T>
    return aClass.getConstructor(Context::class.java).newInstance(this)
}
//inline reified nmg zvat iz javeeee
fun <T>decompile(t: T):ContentValues{

    return ContentValues().apply {

        for (filed in t!!::class.java.declaredFields){
            filed.isAccessible=true
            val value=filed.get(t)
            when {
                value is String ->put(filed.name,value )
                value is Long? ->put(filed.name,value )
                value is Boolean ->put(filed.name,value )
            }
        }

    }
}

fun compile(contentValues: ContentValues):Player{
    return Player(
        contentValues.getAsLong(Player::_id.name),
        contentValues.getAsString(Player::firstname.name),
        contentValues.getAsString(Player::lastname.name),
        contentValues.getAsString(Player::picturePath.name),
        contentValues.getAsString(Player::position.name),
        contentValues.getAsBoolean(Player::best.name)
    )
}


fun getSelection(_id:Long)="_id=?"
fun getSelectionArgs(_id:Long)= arrayOf<String>(_id.toString())