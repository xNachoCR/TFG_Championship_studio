package com.example.tfg_championship_studio.adapter_torneos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_championship_studio.databinding.ItemTorneoBinding
import com.example.tfg_championship_studio.objects.Torneos

class TorneosViewHolder (val view: View): RecyclerView.ViewHolder(view){

    val binding = ItemTorneoBinding.bind(view)

    fun render(torneoModel: Torneos){

        binding.itemName.text = torneoModel.name
//        binding.itemNPlayers.text = binding.itemNPlayers.text.toString() + " " + torneoModel.nComp.toString()
//        binding.itemFormat.text = torneoModel.tournament.toString()
//        binding.itemModelPlayer.text = torneoModel.modelPlayer.toString()
        Glide.with(binding.itemSport.context).load(torneoModel.icon).into(binding.itemSport)

    }
}