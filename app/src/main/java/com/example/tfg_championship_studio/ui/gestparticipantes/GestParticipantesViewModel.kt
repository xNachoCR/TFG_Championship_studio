package com.example.tfg_championship_studio.ui.gestparticipantes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg_championship_studio.objects.Equipos
import com.example.tfg_championship_studio.objects.Jugadores

class GestParticipantesViewModel : ViewModel() {

    val playerList = mutableListOf<Jugadores>()
    val teamList = mutableListOf<Equipos>()

    private val _text = MutableLiveData<String>().apply {
        value = "Esta es la ventana de gestión de participantes"
    }
    val text: LiveData<String> = _text
}