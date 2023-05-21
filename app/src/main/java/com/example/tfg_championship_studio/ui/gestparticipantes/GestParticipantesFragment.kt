package com.example.tfg_championship_studio.ui.gestparticipantes

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
import com.example.tfg_championship_studio.R.*
import com.example.tfg_championship_studio.adapter_players_teams.PlayersAdapter
import com.example.tfg_championship_studio.adapter_players_teams.TeamsAdapter
import com.example.tfg_championship_studio.databinding.FragmentGestParticipantesBinding
import com.example.tfg_championship_studio.objects.Equipos
import com.example.tfg_championship_studio.objects.Jugadores


class GestParticipantesFragment : Fragment() {

    private var _binding: FragmentGestParticipantesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gestParticipantesViewModel =
            ViewModelProvider(this).get(GestParticipantesViewModel::class.java)


        _binding = FragmentGestParticipantesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /*
        var managerP = LinearLayoutManager(context)
        var managerT = LinearLayoutManager(context)
        managerP.orientation = LinearLayoutManager.VERTICAL
        managerT.orientation = LinearLayoutManager.VERTICAL
        var decorationP = DividerItemDecoration(context, managerP.orientation)
        var decorationT = DividerItemDecoration(context, managerT.orientation)
        binding.rvPlayers.layoutManager = managerP
        binding.rvTeams.layoutManager = managerT
        binding.rvPlayers.adapter = PlayersAdapter(gestParticipantesViewModel.playerList)
        binding.rvTeams.adapter = TeamsAdapter(gestParticipantesViewModel.teamList)
        binding.rvPlayers.addItemDecoration(decorationP)
        binding.rvTeams.addItemDecoration(decorationT)
        */
        //binding.fab.setOnClickListener { initAlertDialog(gestParticipantesViewModel.playerList, gestParticipantesViewModel.teamList, gestParticipantesViewModel) }

        return root
    }

    private fun initAlertDialog(listaJugadores: MutableList<Jugadores>, listaEquipos: MutableList<Equipos>, gpvm: GestParticipantesViewModel) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(layout.new_player_team_dialog, null)
        var flag = 0
        var nPlayers = 0
        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(string.new_players_teams_dialog_btn_acept) { dialog, which ->
            val competitor = customView.findViewById<Spinner>(R.id.spinner_format_player_team).selectedItem.toString()
            if (competitor == "Equipo"){
                customView.findViewById<Spinner>(R.id.spinner_n_players_player_team).isClickable = true
                flag = 1
                val nPlayers = customView.findViewById<Spinner>(R.id.spinner_n_players_player_team).selectedItem.toString().toInt()
            }
            val name = customView.findViewById<EditText>(R.id.et_player_team_name).text.toString()

            if (flag == 1){
                val team = Equipos(
                    name = name,
                    nPlayers = nPlayers
                )
                gpvm.teamList.add(team)
                initRecyclerViewT(listaEquipos)
            } else{
                val player = Jugadores(
                    name = name
                )
                gpvm.playerList.add(player)
                initRecyclerViewP(listaJugadores)
            }
        }.setNegativeButton(R.string.new_players_teams_dialog_btn_cancel) { dialog, which ->
            //Lógica del botón
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initRecyclerViewT(lista: MutableList<Equipos>) {
        var listaAux = mutableListOf<Equipos>()
        binding.rvTeams.adapter?.notifyDataSetChanged()
        listaAux = lista
        binding.rvTeams.adapter = TeamsAdapter(listaAux)
    }

    private fun initRecyclerViewP(lista: MutableList<Jugadores>) {
        var listaAux = mutableListOf<Jugadores>()
        binding.rvPlayers.adapter?.notifyDataSetChanged()
        listaAux = lista
        binding.rvPlayers.adapter = PlayersAdapter(listaAux)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}