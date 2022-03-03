package hr.algebra.ivanabilic.nba

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.ivanabilic.nba.framework.startActivity
import hr.algebra.ivanabilic.nba.model.Player
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class PlayersAdapter(private val context: Context,private val players:MutableList<Player>)
    : RecyclerView.Adapter<PlayersAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        private val ivPlayer=itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvPlayer=itemView.findViewById<TextView>(R.id.tvItem)

        fun bind(player: Player) {
            tvPlayer.text=player.toString()
            Picasso.get()
                .load(File(player.picturePath))
                .error(R.drawable.kobe)
                .transform(RoundedCornersTransformation(50,5))
                .into(ivPlayer)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.item_player,parent,false))

    override fun onBindViewHolder(holder: PlayersAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            context.startActivity<PlayerPagerActivity>(ITEM_POSITION,position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.sure)+"'${players[position]}'?")
                setIcon(R.drawable.kobe)
                setCancelable(true)
                setNegativeButton("OK"){_,_-> deleteItem(position)}
                show()
            }
            true
        }
        holder.bind(players[position])
    }

    private fun deleteItem(position: Int) {
        val player=players[position]
        context.contentResolver.delete(ContentUris.withAppendedId(NBA_PROVIDER_URI,player._id!!),null,null)
        File(player.picturePath).delete()
        players.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = players.size
}