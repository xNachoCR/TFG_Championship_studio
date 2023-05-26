package com.example.tfg_championship_studio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_championship_studio.adapter_bracket.BracketAdapter
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentBracketBinding
import com.example.tfg_championship_studio.objects.Jugadores
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bracket.newInstance] factory method to
 * create an instance of this fragment.
 */
class Bracket : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentBracketBinding
    var nPlayers = 0
    var participantesList = mutableListOf<Pair<String, String>>()
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
        binding = FragmentBracketBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        initBracket()
        getNParticipantes()


        return binding.root
    }

    private fun initBracket(){
        val empty = mutableListOf<Pair<String, String>>()
        var manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        var decoration = DividerItemDecoration(context, manager.orientation)
        binding.rvBracket.layoutManager = manager
        binding.rvBracket.addItemDecoration(decoration)
        binding.rvBracket.adapter = BracketAdapter(empty)

    }

    private fun getEmparejamientos(){
        println("El número de jugadores es: " + nPlayers)
        when(nPlayers){
            2 -> {
                val ronda :String = "Final"
                getFase(ronda)
            }
            in 3..4 -> {
                val ronda :String = "Semifinal"
                getFase(ronda)
            }
            in 5..8 -> {
                val ronda :String = "Cuartos"
                getFase(ronda)
            }
            in 9..16 -> {
                val ronda :String = "Octavos"
                getFase(ronda)
            }
            in 17..32 -> {
                val ronda :String = "Dieciseisavos"
                getFase(ronda)
            }
            in 33..64 -> {
                val ronda :String = "Treintaidosavos"
                getFase(ronda)
            }
        }

    }

    private fun getFase(ronda: String) {
        var nEmparejamientos: Int
        if (nPlayers % 2 == 0){
            nEmparejamientos = nPlayers / 2
        } else{
            nEmparejamientos = (nPlayers / 2) + 1
        }
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val emparejamientosData = documentSnapshot.get("Emparejamientos") as? Map<*, *>
                    val finalData = emparejamientosData?.get(TorneosAdapter.GlobalData.idKey.toString()) as? Map<*, *>
                    val emparejamientoData = finalData?.get(ronda) as? Map<*, *>
                    for (i in 1 .. nEmparejamientos){
                        val emparejamiento = emparejamientoData?.get("Emparejamiento$i") as? Map<*, *>
                        val equipo1 = emparejamiento?.get("Equipo1")
                        val equipo2 = emparejamiento?.get("Equipo2")
                        participantesList.add(equipo1.toString() to equipo2.toString())

                    }

                    var manager = LinearLayoutManager(context)
                    manager.orientation = LinearLayoutManager.VERTICAL
                    var decoration = DividerItemDecoration(context, manager.orientation)
                    binding.rvBracket.layoutManager = manager
                    binding.rvBracket.addItemDecoration(decoration)
                    binding.rvBracket.adapter = BracketAdapter(participantesList)

                } else {
                    println("El documento no existe.")
                }
            }
            .addOnFailureListener { e ->
                println("Error al obtener los datos: $e")
            }
    }

    private fun getNParticipantes() {
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val usersData = documentSnapshot.data
                val torneoData = usersData?.get("Torneo") as? Map<String, Any>

                if (torneoData != null) {
                    val championsData = torneoData[TorneosAdapter.GlobalData.nameKey] as? Map<String, Any>

                    if (championsData != null) {
                        val nParticipantes = championsData["nParticipantes"]
                        println("Dato obtenido: " + nParticipantes)
                        nPlayers = nParticipantes.toString().toInt()
                        getEmparejamientos()
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

}