package com.example.tfg_championship_studio.ui.mistorneos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg_championship_studio.objects.Torneos

class MisTorneosViewModel : ViewModel() {

    var listaTorneos = mutableListOf<Torneos>()

    private val _text = MutableLiveData<String>().apply {
        value = "Esta es la ventana de gestión de tus torneos"
    }
    val text: LiveData<String> = _text
}