package com.example.tfg_championship_studio.ui.mistorneos

import android.content.Intent
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
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentMisTorneosBinding
import com.example.tfg_championship_studio.objects.ModelPlayer
import com.example.tfg_championship_studio.objects.Torneos
import com.example.tfg_championship_studio.objects.TournamentType

class MisTorneosFragment : Fragment() {

    private var _binding: FragmentMisTorneosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misTorneosViewModel =
            ViewModelProvider(this).get(MisTorneosViewModel::class.java)

        _binding = FragmentMisTorneosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var listaTorneos = mutableListOf<Torneos>()

        binding.fab.setOnClickListener { initAlertDialog(listaTorneos) }


        return root
    }

    private fun initAlertDialog(listaTorneos: MutableList<Torneos>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.new_tournament_dialog, null)

        val builder = AlertDialog.Builder(context).setView(customView).setPositiveButton(R.string.new_tournament_dialog_btn_acept) { dialog, which ->
            val nPlayers = customView.findViewById<Spinner>(R.id.spinner_n_players).selectedItem.toString().toInt()
            val modelPlayer = customView.findViewById<Spinner>(R.id.spinner_format).selectedItem.toString()
            val tournament = customView.findViewById<Spinner>(R.id.spinner_bracket).selectedItem.toString()
            val name = customView.findViewById<EditText>(R.id.et_tournament_name).text.toString()
            val sport = customView.findViewById<Spinner>(R.id.spinner_sport).selectedItem.toString()
            var icon = 0
            if (sport.toString() == "Fútbol"){
                icon = R.drawable.futbol_ball
            } else {
                icon = R.drawable.google
            }

            println(name)
            val torneo = Torneos(
                icon = icon,
                name = name,
                nComp = nPlayers,
                tournament = tournament,
                modelPlayer = modelPlayer
            )
            listaTorneos.add(torneo)
            initRecyclerView(listaTorneos)
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel) { dialog, which ->
            //Lógica del botón
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun initRecyclerView(listaTorneos: MutableList<Torneos>) {
        var manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        var decoration = DividerItemDecoration(context, manager.orientation)
        binding.rvTorneos.layoutManager = manager
        binding.rvTorneos.adapter = TorneosAdapter(listaTorneos)
        binding.rvTorneos.addItemDecoration(decoration)
    }

    /*
    private fun addTorneo(torneosList: MutableList<Torneos>){
        val torneo = Torneos(
            icon = R.drawable.google,
            name = "Prueba",
            nComp = 8,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo2 = Torneos(
            icon = R.drawable.google,
            name = "Hola que tal",
            nComp = 10,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        torneosList.add(torneo)
        torneosList.add(torneo2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}