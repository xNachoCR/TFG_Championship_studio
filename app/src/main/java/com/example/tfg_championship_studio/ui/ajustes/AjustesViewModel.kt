package com.example.tfg_championship_studio.ui.ajustes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AjustesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Esta es la ventana de ajustes"
    }
    val text: LiveData<String> = _text


}