package com.example.tfg_championship_studio.adapter_bracket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.objects.Jugadores

class BracketAdapter (private val bracketList: List<Pair<String, String>>): RecyclerView.Adapter<BracketViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BracketViewHolder(layoutInflater.inflate(R.layout.item_bracket, parent, false))
    }

    override fun onBindViewHolder(holder: BracketViewHolder, position: Int) {
        val item = bracketList[position]
        holder.render(item)

        holder.binding.tvWin1.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return bracketList.size
    }
}