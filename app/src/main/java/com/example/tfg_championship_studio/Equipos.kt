package com.example.tfg_championship_studio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_championship_studio.adapter_players_teams.PlayersAdapter
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentEquiposBinding
import com.example.tfg_championship_studio.objects.Jugadores


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

object bracket{
    var enfrentamientos = mutableListOf<Pair<Jugadores, Jugadores>>()
}

class Equipos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEquiposBinding
    private val participantesList = mutableListOf<Jugadores>()
    private var restPlayers = TorneosAdapter.GlobalData.nPlayers
    var nPlayers = restPlayers

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
        val equiposViewModel = ViewModelProvider(this)[EquiposViewModel::class.java]

        binding = FragmentEquiposBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        println(TorneosAdapter.GlobalData.format)
        println(TorneosAdapter.GlobalData.nPlayers)

        var restPlayers = TorneosAdapter.GlobalData.nPlayers

        binding.tvParticipantes.text = binding.tvParticipantes.text.toString() + restPlayers.toString()

        var manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        var decoration = DividerItemDecoration(context, manager.orientation)
        binding.rvParticipantes.layoutManager = manager
        binding.rvParticipantes.adapter = PlayersAdapter(participantesList)
        binding.rvParticipantes.addItemDecoration(decoration)

        binding.tvAddParticipantes.setOnClickListener { addParticipante(participantesList) }
        binding.tvGenPartidos.setOnClickListener { generarBracket(participantesList) }

        return binding.root
    }

    private fun generarBracket(pList: MutableList<Jugadores>): MutableList<Pair<Jugadores, Jugadores>> {
        val jugadoresAleatorios = pList.shuffled()

        for (i in 0 until jugadoresAleatorios.size / 2) {
            val jugador1 = jugadoresAleatorios[i * 2]
            val jugador2 = jugadoresAleatorios[i * 2 + 1]
            bracket.enfrentamientos.add(jugador1 to jugador2)
        }

        return bracket.enfrentamientos
    }

    private fun addParticipante(pList: MutableList<Jugadores>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.dialog_new_participante, null)
        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(R.string.new_players_teams_dialog_btn_acept){_, _ ->
            val name = customView.findViewById<EditText>(R.id.et_name_participante).text.toString()
            val participante = Jugadores(
                name = name
            )
            nPlayers -= 1
            if (nPlayers == 0){
                binding.tvAddParticipantes.visibility = View.GONE
                binding.tvGenPartidos.visibility = View.VISIBLE
            }
            binding.tvParticipantes.text = "Participantes restantes por añadir: " + nPlayers.toString()
            pList.add(participante)
            initRecyclerView(pList)
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel){ _, _ ->
            //Lógica del botón
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initRecyclerView(pList: MutableList<Jugadores>) {
        var listaAux = mutableListOf<Jugadores>()
        binding.rvParticipantes.adapter?.notifyDataSetChanged()
        listaAux = pList
        binding.rvParticipantes.adapter = PlayersAdapter(listaAux)
    }

}

