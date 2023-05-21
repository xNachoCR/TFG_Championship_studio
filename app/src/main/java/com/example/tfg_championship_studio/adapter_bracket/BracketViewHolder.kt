package com.example.tfg_championship_studio.adapter_bracket

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.databinding.ItemBracketBinding
import com.example.tfg_championship_studio.objects.Jugadores

class BracketViewHolder (val view: View): RecyclerView.ViewHolder(view){
    val binding = ItemBracketBinding.bind(view)

    fun render(playerModel: Pair<Jugadores, Jugadores>){
        binding.tvTeam1.text = playerModel.first.name
        binding.tvTeam2.text = playerModel.second.name
    }
}