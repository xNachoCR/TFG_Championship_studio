package com.example.tfg_championship_studio.adapter_players_teams

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.databinding.ItemPlayerBinding
import com.example.tfg_championship_studio.objects.Jugadores

class PlayersViewHolder (val view: View): RecyclerView.ViewHolder(view){

    val binding = ItemPlayerBinding.bind(view)

    fun render(playerModel: Jugadores){
        binding.tvName.text = playerModel.name
    }
}