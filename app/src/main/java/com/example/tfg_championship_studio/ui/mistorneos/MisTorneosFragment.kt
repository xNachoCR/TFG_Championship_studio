package com.example.tfg_championship_studio.ui.mistorneos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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

        binding.fab.setOnClickListener { initRecyclerView(listaTorneos) }


        return root
    }


    private fun initRecyclerView(listaTorneos: MutableList<Torneos>) {
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, manager.orientation)
        addTorneo(listaTorneos)
        binding.rvTorneos.layoutManager = manager
        binding.rvTorneos.adapter = TorneosAdapter(listaTorneos)
        binding.rvTorneos.addItemDecoration(decoration)

    }

    private fun addTorneo(torneosList: MutableList<Torneos>){
        val torneo = Torneos(
            icon = "https://cursokotlin.com/wp-content/uploads/2020/09/Webp.net-compress-image.jpg",
            name = "Prueba",
            nComp = 8,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo2 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Hola que tal",
            nComp = 10,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo3 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Prueba",
            nComp = 8,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo4 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Hola que tal",
            nComp = 10,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo5 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Prueba",
            nComp = 8,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo6 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Hola que tal",
            nComp = 10,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo7 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Prueba",
            nComp = 8,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        val torneo8 = Torneos(
            icon = R.drawable.google.toString(),
            name = "Hola que tal",
            nComp = 10,
            tournament = TournamentType.LEAGUE,
            modelPlayer = ModelPlayer.SINGLES
        )

        torneosList.add(torneo)
        torneosList.add(torneo2)
        torneosList.add(torneo3)
        torneosList.add(torneo4)
        torneosList.add(torneo5)
        torneosList.add(torneo6)
        torneosList.add(torneo7)
        torneosList.add(torneo8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}