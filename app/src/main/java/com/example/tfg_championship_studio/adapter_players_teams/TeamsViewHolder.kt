package com.example.tfg_championship_studio.adapter_players_teams

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.databinding.ItemTeamBinding
import com.example.tfg_championship_studio.objects.Equipos
import com.example.tfg_championship_studio.objects.Jugadores

class TeamsViewHolder (val view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemTeamBinding.bind(view)

    fun render(teamModel: Equipos){
        binding.tvName.text = teamModel.name
    }
}