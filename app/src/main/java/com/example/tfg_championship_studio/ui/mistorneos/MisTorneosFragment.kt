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
import com.example.tfg_championship_studio.adapter_torneos.TorneosAdapter
import com.example.tfg_championship_studio.databinding.FragmentMisTorneosBinding
import com.example.tfg_championship_studio.objects.Torneos

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
            ViewModelProvider(this)[MisTorneosViewModel::class.java]

        _binding = FragmentMisTorneosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rvTorneos.layoutManager = manager
        binding.rvTorneos.adapter = TorneosAdapter(misTorneosViewModel.listaTorneos)
        binding.rvTorneos.addItemDecoration(decoration)

        binding.fab.setOnClickListener { initAlertDialog(misTorneosViewModel.listaTorneos) }

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

            val torneo = Torneos(
                icon = icon,
                name = name,
//                nComp = nPlayers,
//                tournament = tournament,
//                modelPlayer = modelPlayer
            )
            listaTorneos.add(torneo)
            initRecyclerView(listaTorneos)
        }.setNegativeButton(R.string.new_tournament_dialog_btn_cancel) { _, _ ->
            //Lógica del botón
        }

        val dialog = builder.create()
        dialog.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(listaTorneos: MutableList<Torneos>) {
        binding.rvTorneos.adapter?.notifyDataSetChanged()
        val listaTorneosAux: MutableList<Torneos> = listaTorneos
        binding.rvTorneos.adapter = TorneosAdapter(listaTorneosAux)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}