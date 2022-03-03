package hr.algebra.ivanabilic.nba

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.algebra.ivanabilic.nba.api.NbaFetcher
import hr.algebra.ivanabilic.nba.framework.enqueue

class NbaService(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context,workerParameters){

    companion object{
        fun enqueue(context: Context){
            WorkManager.getInstance(context).enqueue<NbaService>()
        }
    }

    override fun doWork(): Result {
        NbaFetcher(context).fetchPlayers()
        return Result.success()
    }
}