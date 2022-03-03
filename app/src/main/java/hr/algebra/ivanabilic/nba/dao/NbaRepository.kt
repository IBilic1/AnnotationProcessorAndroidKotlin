package hr.algebra.ivanabilic.nba.dao

import android.content.ContentValues
import android.database.Cursor
import hr.algebra.ivanabilic.annotations.*
import hr.algebra.ivanabilic.nba.model.Player

@Dao("Players")
interface NbaRepository {
    @Delete
    fun delete(_id:Long?): Int
    @Update
    fun update(player: Player): Int
    @Insert
    fun insert(player: Player): Long
    @Query
    fun query(): Cursor
}