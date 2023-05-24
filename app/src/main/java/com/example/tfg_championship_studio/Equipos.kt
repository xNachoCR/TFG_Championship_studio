package com.example.tfg_championship_studio

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_championship_studio.adapter_players_teams.PlayersAdapter
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentEquiposBinding
import com.example.tfg_championship_studio.objects.Jugadores
import com.example.tfg_championship_studio.objects.Torneos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.logging.Handler


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

object cuadro{
    var enfrentamientos = mutableListOf<Pair<Jugadores, Jugadores>>()
}

class Equipos : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEquiposBinding
    private var participantesList = mutableListOf<Jugadores>()
    private var nPlayers = 0
    val db = FirebaseFirestore.getInstance()
    val documentRef = db.collection("users").document(SignUpActivity.GlobalData.emailKey)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEquiposBinding.inflate(inflater, container, false)

        binding.tvAddParticipantes.visibility = View.VISIBLE
        binding.tvGenPartidos.visibility = View.GONE
        println(TorneosAdapter.GlobalData.nameKey)
        getParticipantes()
        get_nPlayers()


        binding.tvAddParticipantes.setOnClickListener { addParticipante(participantesList) }
        binding.tvGenPartidos.setOnClickListener { generarBracket(participantesList) }

        return binding.root
    }



    private fun generarBracket(pList: MutableList<Jugadores>): MutableList<Pair<Jugadores, Jugadores>> {
        val jugadoresAleatorios = pList.shuffled()
        val bracket = Bracket()

        for (i in 0 until jugadoresAleatorios.size / 2) {
            val jugador1 = jugadoresAleatorios[i * 2]
            val jugador2 = jugadoresAleatorios[i * 2 + 1]
            cuadro.enfrentamientos.add(jugador1 to jugador2)
        }

        binding.tvAddParticipantes.visibility = View.GONE
        binding.tvGenPartidos.visibility = View.GONE
        binding.tvParticipantes.text = "PARTICIPANTES DEL TORNEO"
        showLongSnackbar(binding.root)

        return cuadro.enfrentamientos
    }

    private fun addParticipante(pList: MutableList<Jugadores>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.dialog_new_participante, null)
        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(R.string.new_players_teams_dialog_btn_acept) { _, _ ->
            val name = customView.findViewById<EditText>(R.id.et_name_participante).text.toString()
            val participante = Jugadores(name = name)
            nPlayers -= 1
            if (nPlayers == 0) {
                binding.tvAddParticipantes.visibility = View.GONE
                binding.tvGenPartidos.visibility = View.VISIBLE
            }
            binding.tvParticipantes.text = "Participantes restantes por añadir: " + nPlayers.toString()
            saveParticipante(name)
            pList.add(participante)
            initRecyclerView(pList)
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel) { _, _ ->
            // Lógica del botón Cancelar
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun saveParticipante(name: String) {

    }

    private fun initRecyclerView(pList: MutableList<Jugadores>) {
        binding.rvParticipantes.adapter?.notifyDataSetChanged()
        binding.rvParticipantes.adapter = PlayersAdapter(pList.toMutableList())
    }

    private fun getParticipantes() {
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val torneoData = documentSnapshot.data
                val torneo = torneoData?.get("Torneo") as? Map<String, Any>
                val championsData = torneo?.get(TorneosAdapter.GlobalData.nameKey) as? Map<String, Any>
                val participanteData = championsData?.get("Participante") as? Map<String, Map<String, Any>>
                if (participanteData != null) {
                    val nombresMapasVacios = participanteData.keys
                    for (name in nombresMapasVacios) {
                        val jugador = Jugadores(name = name)
                        participantesList.add(jugador)
                    }
                } else {
                    println("El mapa Participante no existe o no es un mapa")
                }
            } else {
                println("El documento no existe")
            }
        }.addOnFailureListener { e ->
            participantesList = mutableListOf<Jugadores>()
            println("Error al obtener el documento: $e")
        }
    }

    private fun get_nPlayers(){
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val usersData = documentSnapshot.data
                val torneoData = usersData?.get("Torneo") as? Map<String, Any>

                if (torneoData != null) {
                    val championsData = torneoData[TorneosAdapter.GlobalData.nameKey] as? Map<String, Any>

                    if (championsData != null) {
                        val nParticipantes = championsData["nParticipantes"]
                        nPlayers = nParticipantes.toString().toInt()
                        if (nPlayers == participantesList.size && nPlayers != 0){
                            binding.tvAddParticipantes.visibility = View.GONE
                            binding.tvGenPartidos.visibility = View.VISIBLE
                        }

                        val manager = LinearLayoutManager(context)
                        manager.orientation = LinearLayoutManager.VERTICAL
                        val decoration = DividerItemDecoration(context, manager.orientation)
                        binding.rvParticipantes.layoutManager = manager
                        binding.rvParticipantes.addItemDecoration(decoration)

                        binding.tvParticipantes.text = binding.tvParticipantes.text.toString() + nPlayers.toString()
                        initRecyclerView(participantesList)
                    } else {
                        println("El objeto Champions no existe o no es un mapa")
                    }
                } else {
                    println("Torneo no existe")
                }
            } else {
                println("El documento no existe")
            }
        }.addOnFailureListener { e ->
            println("Error al obtener el documento: $e")
        }
    }
    fun showLongSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "Se han generado los partidos en la pestaña bracket", Snackbar.LENGTH_LONG).show()
    }
}
