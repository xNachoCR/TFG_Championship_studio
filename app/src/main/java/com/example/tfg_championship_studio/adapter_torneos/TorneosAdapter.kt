package com.example.tfg_championship_studio.adapter_torneos


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.BottomMainActivity
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.objects.Futbol
import com.example.tfg_championship_studio.objects.Torneos


class TorneosAdapter(private val context: Context, private val torneosList: MutableList<Torneos>): RecyclerView.Adapter<TorneosViewHolder> () {

    object GlobalData{
        var nPlayers = 0
        var format = ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TorneosViewHolder(layoutInflater.inflate(R.layout.item_torneo, parent, false))
    }

    val futbol = R.drawable.img_futbol_ball
    val basket = R.drawable.img_basketball
    val tenis = R.drawable.img_tenis
    val motorsport = R.drawable.img_motorsport

    override fun onBindViewHolder(holder: TorneosViewHolder, position: Int) {
        val item = torneosList[position]
        holder.render(item)
        holder.binding.tvDelete.setOnClickListener {
            torneosList.removeAt(position)
            println(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, torneosList.size)
        }

        holder.binding.tvEdit.setOnClickListener {
            showBottomMain()
        }

        holder.binding.tvConfig.setOnClickListener {
            when (torneosList[position].icon) {
                futbol -> {
                    alertDialogFutbol(holder,torneosList)
                }
                tenis -> {

                }
                basket -> {

                }
                motorsport -> {

                }
            }

            holder.binding.tvEdit.visibility = View.VISIBLE
            holder.binding.tvConfig.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return torneosList.size
    }

    private fun showBottomMain() {
        val mainIntent = Intent(context, BottomMainActivity::class.java)
        context.startActivity(mainIntent)
    }

    private fun alertDialogFutbol(holder: TorneosViewHolder, torneosList: MutableList<Torneos>) {
        val inflater = LayoutInflater.from(holder.view.context)
        val customView = inflater.inflate(R.layout.item_futbol_config, null)
        val builder = AlertDialog.Builder(holder.view.context).setView(customView).setPositiveButton(R.string.new_tournament_dialog_btn_acept) {_, _ ->
            GlobalData.nPlayers = customView.findViewById<Spinner>(R.id.spinner_n_players).selectedItem.toString().toInt()
            GlobalData.format = customView.findViewById<Spinner>(R.id.spinner_format).selectedItem.toString()
        }

        val dialog = builder.create()
        dialog.show()


    }
}



