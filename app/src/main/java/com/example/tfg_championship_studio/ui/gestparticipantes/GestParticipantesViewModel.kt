package com.example.tfg_championship_studio.ui.gestparticipantes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GestParticipantesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Esta es la ventana de gestión de participantes"
    }
    val text: LiveData<String> = _text
}