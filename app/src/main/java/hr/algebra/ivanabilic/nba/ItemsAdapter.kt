package hr.algebra.ivanabilic.nba

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(private val context: Context,private val items:MutableList<MenuItem>)
    :RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        private val tvItem=itemView.findViewById<TextView>(R.id.tvItem)
        val cbSelect=itemView.findViewById<CheckBox>(R.id.cbSelect)
        var selected=true
        fun bind(menuItem: MenuItem) {
            tvItem.setText(menuItem.title)
            cbSelect.isSelected=selected
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.list_menu,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cbSelect.setOnClickListener {
            holder.selected=!holder.selected
            items[position].setVisible(holder.selected)
        }
        holder.bind(items[position])
    }

    override fun getItemCount()=items.size
}