package com.example.tfg_championship_studio.adapter_torneos


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_championship_studio.BottomMainActivity
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.SignUpActivity
import com.example.tfg_championship_studio.objects.Torneos
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class TorneosAdapter(private val context: Context, private val torneosList: MutableList<Torneos>): RecyclerView.Adapter<TorneosViewHolder> () {

    val db = FirebaseFirestore.getInstance()
    val documentRef = FirebaseFirestore.getInstance().collection("users").document(SignUpActivity.GlobalData.emailKey)

    object GlobalData{
        var nameKey = ""
        var idKey = 0
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

        if (torneosList[position].estado){
            holder.binding.tvConfig.visibility = View.GONE
            holder.binding.tvEdit.visibility = View.VISIBLE
        }

        holder.binding.tvDelete.setOnClickListener { deleteTorneo(item, position)}
        holder.binding.tvEdit.setOnClickListener { gestionarTorneo(position) }

        holder.binding.tvConfig.setOnClickListener {
            when (torneosList[position].icon) {
                futbol -> {
                    preparaBracket(holder ,position)
                    alertDialogFutbol(holder,torneosList, position)
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

    private fun gestionarTorneo(position: Int) {
        GlobalData.nameKey = torneosList[position].name
        GlobalData.idKey = torneosList[position].id
        showBottomMain()
    }

    private fun preparaBracket(holder: TorneosViewHolder,position: Int) {
        val emparejamientosData = HashMap<String, Any>()
        emparejamientosData[torneosList[position].id.toString()] = HashMap<String, Any>()

        documentRef.set(mapOf("Emparejamientos" to emparejamientosData), SetOptions.merge())
            .addOnSuccessListener {
                println("Se agregó el nuevo mapa correctamente en la colección 'Emparejamientos'.")
            }
            .addOnFailureListener { e ->
                println("Error al agregar el nuevo mapa en la colección 'Emparejamientos': $e")
            }

        holder.binding.tvConfig.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return torneosList.size
    }

    private fun showBottomMain() {
        val mainIntent = Intent(context, BottomMainActivity::class.java)
        context.startActivity(mainIntent)
    }

    private fun alertDialogFutbol(holder: TorneosViewHolder, torneosList: MutableList<Torneos>, position: Int) {
        val inflater = LayoutInflater.from(holder.view.context)
        val customView = inflater.inflate(R.layout.item_futbol_config, null)
        val builder = AlertDialog.Builder(holder.view.context).setView(customView).setPositiveButton(R.string.new_tournament_dialog_btn_acept) {_, _ ->
            val documentRef = db.collection("users").document(SignUpActivity.GlobalData.emailKey)
            documentRef.update("Torneo." + torneosList[position].name + ".nParticipantes",customView.findViewById<Spinner>(R.id.spinner_n_players).selectedItem.toString().toInt())
            documentRef.update("Torneo." + torneosList[position].name + ".estado",true)
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel) { _, _ ->
            //Lógica del botón
        }


        val dialog = builder.create()
        dialog.show()


    }

    private fun deleteTorneo(item: Torneos, position: Int){
        torneosList.removeAt(position)
        println(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, torneosList.size)
        val name = item.name
        val idT = item.id
        val updateT = hashMapOf<String, Any>(
            "Torneo." + name to FieldValue.delete()
        )
        val updateE = hashMapOf<String, Any>(
            "Emparejamientos." + idT to FieldValue.delete()
        )
        documentRef.update(updateT)
        documentRef.update(updateE)
    }
}



