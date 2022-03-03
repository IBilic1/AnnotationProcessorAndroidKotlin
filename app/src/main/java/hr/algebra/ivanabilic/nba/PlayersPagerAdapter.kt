package hr.algebra.ivanabilic.nba

import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
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
import hr.algebra.ivanabilic.nba.model.Positions
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class PlayersPagerAdapter(private val context: Context, private val players:MutableList<Player>)
    : RecyclerView.Adapter<PlayersPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        private val ivPlayer=itemView.findViewById<ImageView>(R.id.ivPlayer)
        private val tvFirstName=itemView.findViewById<TextView>(R.id.tvFirstName)
        private val tvLastName=itemView.findViewById<TextView>(R.id.tvLastName)
        private val tvPosition=itemView.findViewById<TextView>(R.id.tvPosition)
        val ivBest=itemView.findViewById<ImageView>(R.id.ivBest)

        fun bind(player: Player) {
            tvFirstName.text=player.firstname
            tvLastName.text=player.lastname
            tvPosition.text= Positions.getPositions(player.position)

            Picasso.get()
                .load(File(player.picturePath))
                .error(R.drawable.kobe)
                .transform(RoundedCornersTransformation(50,5))
                .into(ivPlayer)

            ivBest.setImageResource(
                if (player.best) R.drawable.best_gold else R.drawable.best_black)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.pager_player,parent,false))

    override fun onBindViewHolder(holder: PlayersPagerAdapter.ViewHolder, position: Int) {
        val  player=players[position]
        holder.ivBest.setOnClickListener {
            player.best=!player.best
            val uri=ContentUris.withAppendedId(NBA_PROVIDER_URI,player._id!!)
            val values=ContentValues().apply {
                put(Player::_id.name,player._id)
                put(Player::firstname.name,player.firstname)
                put(Player::lastname.name,player.lastname)
                put(Player::picturePath.name,player.picturePath)
                put(Player::position.name,player.position)
                put(Player::best.name,player.best)
            }
            context.contentResolver.update(
                uri,
                values,
                null,
                null
            )
            notifyItemChanged(position)
        }
        holder.bind(players[position])
    }

    override fun getItemCount() = players.size
}