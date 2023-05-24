package com.example.tfg_championship_studio.adapter_players_teams

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.databinding.ItemTeamBinding

class TeamsViewHolder (val view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemTeamBinding.bind(view)

    fun render(teamModel: String){//Equipos){
        binding.tvName.text = ""//teamModel.name
    }
}