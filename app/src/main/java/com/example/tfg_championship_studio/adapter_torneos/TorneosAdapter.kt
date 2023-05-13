package com.example.tfg_championship_studio.adapter_torneos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.objects.Torneos



class TorneosAdapter(private val torneosList: MutableList<Torneos>): RecyclerView.Adapter<TorneosViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TorneosViewHolder(layoutInflater.inflate(R.layout.item_torneo, parent, false))
    }

    override fun onBindViewHolder(holder: TorneosViewHolder, position: Int) {
        val item = torneosList[position]
        holder.render(item)
        holder.binding.tvDelete.setOnClickListener {
            torneosList.removeAt(position)
            println(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, torneosList.size)
        }
    }

    override fun getItemCount(): Int {
        return torneosList.size
    }

}

