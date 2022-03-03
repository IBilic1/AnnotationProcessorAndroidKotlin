package hr.algebra.ivanabilic.nba

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.ivanabilic.nba.dao.NbaRepository
import hr.algebra.ivanabilic.nba.dao.NbaSqlHelper
import hr.algebra.ivanabilic.nba.framework.compile
import hr.algebra.ivanabilic.nba.model.Player
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.ivanabilic.nba.api.provider"
private const val PATH = "players"
private const val PLAYERS = 10
private const val PLAYER_ID = 20

val NBA_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, PLAYERS)
    addURI(AUTHORITY, "$PATH/#", PLAYER_ID)
    this
}

class NbaProvider :ContentProvider() {

    private lateinit var nbaRepository: NbaRepository

    override fun onCreate(): Boolean {
        nbaRepository=NbaSqlHelper.getInstance(context!!)?.getNbaRepository()!!
        return true
    }

    override fun query(
        p0: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor? {
        return nbaRepository.query()
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, values: ContentValues?): Uri? {
        val player=Player(
            values?.getAsLong(Player::_id.name),
            values?.getAsString(Player::firstname.name)!!,
            values?.getAsString(Player::lastname.name),
            values?.getAsString(Player::picturePath.name),
            values?.getAsString(Player::position.name),
            values?.getAsBoolean(Player::best.name)
        )
        val id = nbaRepository.insert(player)
        return ContentUris.withAppendedId(NBA_PROVIDER_URI,id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)){
            PLAYERS-> {
                return nbaRepository.delete(null)
            }
            PLAYER_ID->{
                uri.lastPathSegment?.let {
                    println(it.toString())
                    return nbaRepository.delete(it.toLong())
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)){
            PLAYERS->return nbaRepository.update(compile(values!!))
            PLAYER_ID->{
                uri.lastPathSegment?.let {
                    val player=compile(values!!)
                    player._id=it.toString().toLong()
                    return nbaRepository.update(player)
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}