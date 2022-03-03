package hr.algebra.ivanabilic.nba.dao

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.ivanabilic.annotations.Database
import hr.algebra.ivanabilic.nba.framework.getGeneratedImplementation

private const val DB_NAME = "players.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "Players"

private const val CREATE = "create table Players(" +
        "_id integer primary key autoincrement, " +
        "firstname text not null, " +
        "lastname text not null, " +
        "picturePath text not null, " +
        "position text not null, " +
        "best integer not null" + ")"
@Database(createStatement = CREATE)
abstract class NbaSqlHelper(context: Context?)  : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    abstract fun getNbaRepository() : NbaRepository?

    companion object{

        @Volatile private var INSTANCE :NbaSqlHelper?=null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(NbaSqlHelper::class.java){
                INSTANCE ?: buildDatabase(context).also{
                    INSTANCE=it
                }
            }

        private fun buildDatabase(context: Context): NbaSqlHelper? {
            return context.getGeneratedImplementation<NbaSqlHelper>("_impl")
        }
    }


}