package com.example.tfg_championship_studio

import androidx.lifecycle.ViewModel
import com.example.tfg_championship_studio.objects.Jugadores

class EquiposViewModel : ViewModel() {
    val participantesList = mutableListOf<Jugadores>()
}