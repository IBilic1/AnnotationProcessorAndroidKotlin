package hr.algebra.ivanabilic.nba

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.ivanabilic.nba.framework.startActivity

class NbaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        context.startActivity<HostActivity>()
    }
}