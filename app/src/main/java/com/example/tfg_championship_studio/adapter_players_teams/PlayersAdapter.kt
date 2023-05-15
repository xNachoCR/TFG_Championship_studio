package com.example.tfg_championship_studio.adapter_players_teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.objects.Jugadores


class PlayersAdapter(private val playersList: MutableList<Jugadores>): RecyclerView.Adapter<PlayersViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlayersViewHolder(layoutInflater.inflate(R.layout.item_player, parent, false))
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val item = playersList[position]
        holder.render(item)
        holder.binding.tvDelete.setOnClickListener {
            playersList.removeAt(position)
            println(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, playersList.size)
        }
    }

    override fun getItemCount(): Int {
        return playersList.size
    }

}