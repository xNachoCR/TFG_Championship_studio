package com.example.tfg_championship_studio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_championship_studio.adapter_players_teams.PlayersAdapter
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentEquiposBinding
import com.example.tfg_championship_studio.objects.Jugadores
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

object GlobalData{
    var emparejamientos = mutableListOf<Pair<String, String>>()
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



    private fun generarBracket(pList: MutableList<Jugadores>) {
        val jugadoresAleatorios: MutableList<Jugadores> = pList.shuffled() as MutableList<Jugadores>
        var enfrentamientos = mutableListOf<Pair<String, String>>()

        when (nPlayers) {
            2 -> {
                enfrentamientos.add(jugadoresAleatorios[0].name to jugadoresAleatorios[1].name)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Final"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
            in 3..4 -> {
                enfrentamientos = emparejarParticipantes(jugadoresAleatorios)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Semifinal"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
            in 5..8 -> {
                enfrentamientos = emparejarParticipantes(jugadoresAleatorios)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Cuartos"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
            in 9..16 -> {
                enfrentamientos = emparejarParticipantes(jugadoresAleatorios)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Octavos"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
            in 17..32 -> {
                enfrentamientos = emparejarParticipantes(jugadoresAleatorios)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Dieciseisavos"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
            in 33..64 -> {
                enfrentamientos = emparejarParticipantes(jugadoresAleatorios)
                GlobalData.emparejamientos = enfrentamientos
                val ronda :String = "Treintaidosavos"
                saveEmparejamientos(ronda)
                cambiaEuiposFragment()
            }
        }
    }


    private fun saveEmparejamientos(ronda: String) {
        val id = TorneosAdapter.GlobalData.idKey
        documentRef.update("Emparejamientos." + id.toString() + "." + ronda, createEmparejamientosMap(GlobalData.emparejamientos.size))
            .addOnSuccessListener {
                println("Se agregaron los emparejamientos correctamente.")
            }
            .addOnFailureListener { e ->
                println("Error al agregar los emparejamientos: $e")
            }
    }

    private fun createEmparejamientosMap(nEmparejamientos: Int): Map<String, Any> {
        val emparejamientosMap = mutableMapOf<String, Any>()

        for (i in 0 until nEmparejamientos) {
            val emparejamientoMap = mutableMapOf<String, Any>()
            emparejamientoMap["Equipo1"] = GlobalData.emparejamientos[i].first
            emparejamientoMap["Equipo2"] = GlobalData.emparejamientos[i].second

            emparejamientosMap["Emparejamiento" + (i + 1)] = emparejamientoMap
        }

        return emparejamientosMap
    }


    private fun addParticipante(pList: MutableList<Jugadores>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.dialog_new_participante, null)
        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(R.string.new_players_teams_dialog_btn_acept) { _, _ ->
            val name = customView.findViewById<EditText>(R.id.et_name_participante).text.toString()
            if(name == "Nombre del campeonato" || name.startsWith(".") || name.endsWith(".") || name.contains("..") || checkName(pList, name) || name.isEmpty()){
                showAlert()
                return@setPositiveButton
            }
            for (i in pList){
                if (name == i.name){
                    showAlert()
                    return@setPositiveButton
                }
            }
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
                        } else if (nPlayers != participantesList.size && participantesList.size != 0){
                            nPlayers -= participantesList.size
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

    private fun saveParticipante(name: String) {
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val torneoData = documentSnapshot.data
                    val torneo = torneoData?.get("Torneo") as? Map<String, Any>
                    val championsData = torneo?.get(TorneosAdapter.GlobalData.nameKey) as? Map<String, Any>
                    val participanteData = championsData?.get("Participante") as? Map<String, Any>

                    if (participanteData != null) {
                        // Verificar si ya existe un mapa vacío en "Participante"
                        if (!participanteData.containsKey(name)) {
                            // Agregar el nuevo mapa vacío al campo "Participante"
                            val newData = hashMapOf<String, Any>()
                            newData.putAll(participanteData)
                            newData[name] = hashMapOf<String, Any>()

                            documentRef.update("Torneo." + TorneosAdapter.GlobalData.nameKey + ".Participante", newData)
                                .addOnSuccessListener {
                                    // Actualización exitosa
                                    println("Nuevo mapa vacío agregado a 'Participante' correctamente.")
                                }
                                .addOnFailureListener { e ->
                                    // Error al realizar la actualización
                                    println("Error al agregar el nuevo mapa vacío a 'Participante': $e")
                                }
                        } else {
                            // El mapa vacío ya existe en "Participante"
                            showAlert()
                            println("El mapa vacío 'NuevoParticipante' ya existe en 'Participante'.")
                        }
                    } else {
                        // El campo "Participante" no es un mapa
                        println("El campo 'Participante' no es un mapa.")
                    }
                } else {
                    // El documento no existe
                    println("El documento no existe.")
                }
            }
            .addOnFailureListener { e ->
                // Error al obtener el documento
                println("Error al obtener el documento: $e")
            }
    }
    fun showLongSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "Se han generado los partidos en la pestaña bracket", Snackbar.LENGTH_LONG).show()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Ya existe un aprticipante con ese nombre")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun emparejarParticipantes(pList: MutableList<Jugadores>): MutableList<Pair<String, String>>{
        var enfrentamientos = mutableListOf<Pair<String, String>>()

        if (nPlayers % 2 == 0) {
            // Enfrentar a los jugadores de dos en dos
            for (i in 0 until nPlayers step 2) {
                enfrentamientos.add(pList[i].name to pList[i + 1].name)
            }

            return enfrentamientos
        } else {
            for (i in 0 until (nPlayers - 2) step 2) {
                enfrentamientos.add(pList[i].name to pList[i + 1].name)
            }
            // Enfrentar al jugador sobrante contra "bye"
            val jugadorSobrante = pList.last()
            enfrentamientos.add(jugadorSobrante.name to "bye")
            return enfrentamientos
        }
    }

    private fun cambiaEuiposFragment() {
        binding.tvAddParticipantes.visibility = View.GONE
        binding.tvGenPartidos.visibility = View.GONE
        binding.tvParticipantes.text = "PARTICIPANTES DEL TORNEO"
        showLongSnackbar(binding.root)
    }

    private fun checkName(listaTorneos: MutableList<Jugadores>, name: String): Boolean {
        for (torneo in listaTorneos) {
            if (torneo.name == name){
                return true
            }
        }

        return false
    }


}
