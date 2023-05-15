package com.example.tfg_championship_studio.adapter_players_teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.objects.Equipos

class TeamsAdapter(private val teamList: MutableList<Equipos>):RecyclerView.Adapter<TeamsViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TeamsViewHolder(layoutInflater.inflate(R.layout.item_team, parent, false))
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        val item = teamList[position]
        holder.render(item)
        holder.binding.tvDelete.setOnClickListener {
            teamList.removeAt(position)
            println(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, teamList.size)
        }
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

}