package com.example.tfg_championship_studio.ui.mistorneos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_championship_studio.R
import com.example.tfg_championship_studio.SignUpActivity
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentMisTorneosBinding
import com.example.tfg_championship_studio.objects.Torneos
import com.google.firebase.firestore.FirebaseFirestore

class MisTorneosFragment : Fragment() {

    private var _binding: FragmentMisTorneosBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var tId = 0
    var listaTorneos = mutableListOf<Torneos>()
    val listaAux = mutableListOf<Torneos>()
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("users")
    val documentId = SignUpActivity.GlobalData.emailKey

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misTorneosViewModel =
            ViewModelProvider(this)[MisTorneosViewModel::class.java]

        _binding = FragmentMisTorneosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listaTorneos = listaAux

        cargaDatos()

        binding.fab.setOnClickListener { initAlertDialog(listaTorneos) }

        return root
    }

    private fun initAlertDialog(listaTorneos: MutableList<Torneos>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.new_tournament_dialog, null)

        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(R.string.new_tournament_dialog_btn_acept) { _, _ ->
//            val nPlayers = customView.findViewById<Spinner>(R.id.spinner_n_players).selectedItem.toString().toInt()
//            val modelPlayer = customView.findViewById<Spinner>(R.id.spinner_format).selectedItem.toString()
//            val tournament = customView.findViewById<Spinner>(R.id.spinner_bracket).selectedItem.toString()
            val name = customView.findViewById<EditText>(R.id.et_tournament_name).text.toString()
            if(name == "Nombre del campeonato" || name.startsWith(".") || name.endsWith(".") || name.contains("..") || checkName(listaTorneos, name) || name.isEmpty()){
                showAlert()
                return@setPositiveButton
            }
            val sport = customView.findViewById<Spinner>(R.id.spinner_sport).selectedItem.toString()
            var icon = 0
            when(sport){
                "Fútbol" -> {
                    icon = R.drawable.img_futbol_ball
                }
                "Tenis" -> {
                    icon = R.drawable.img_tenis
                }
                "Baloncesto" -> {
                    icon = R.drawable.img_basketball
                }
                "Motorsport" -> {
                    icon = R.drawable.img_motorsport
                }
            }
            tId++
            val torneo = Torneos(
                icon = icon,
                name = name,
                id = tId,
                estado = false
            )
            listaTorneos.add(torneo)
            val documentRef = db.collection("users").document(SignUpActivity.GlobalData.emailKey)
            val newTorneoData = hashMapOf(
                "id" to tId,
                "nParticipantes" to 0,
                "estado" to false,
                "Participante" to emptyMap<String, Any>()
            )
            documentRef.update("Torneo." + name, newTorneoData)
            cargaRecyclerView()
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel) { _, _ ->
            //Lógica del botón
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun checkName(listaTorneos: MutableList<Torneos>, name: String): Boolean {
        for (torneo in listaTorneos) {
            if (torneo.name == name){
                return true
            }
        }

        return false
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("El nombre introducido no es válido")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        binding.rvTorneos.adapter?.notifyDataSetChanged()
    }

    private fun cargaDatos(){
        collectionRef.document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val torneoData = documentSnapshot.data
                    val torneoValue = torneoData?.get("Torneo")
                    if (torneoValue is Map<*, *>) {
                        val fieldNames = torneoValue.keys
                        for (fieldName in fieldNames) {
                            val fieldValue = torneoValue[fieldName]
                            if (fieldValue is Map<*, *>){
                                val id = fieldValue["id"]
                                val estado = fieldValue["estado"]
                                tId = id.toString().toInt()
                                println("Id: $id")
                                val torneo = Torneos(
                                    icon = R.drawable.img_futbol_ball,
                                    name = fieldName.toString(),
                                    id = id.toString().toInt(),
                                    estado = estado as Boolean
                                )
                                listaTorneos.add(torneo)
                            }
                            cargaRecyclerView()
                        }
                    } else {
                        println("El campo Torneo no existe en el documento o no es un objeto Map.")
                    }
                } else {
                    println("El documento no existe.")
                }
            }
            .addOnFailureListener { e ->
                println("Error al leer el documento: $e")
            }

    }

    private fun cargaRecyclerView() {
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rvTorneos.layoutManager = manager
        binding.rvTorneos.addItemDecoration(decoration)
        binding.rvTorneos.adapter = context?.let { TorneosAdapter(it,listaTorneos) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

