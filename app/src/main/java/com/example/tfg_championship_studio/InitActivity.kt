package com.example.tfg_championship_studio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_championship_studio.databinding.ActivityInitBinding

class InitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener { loginScreen() }
        binding.btnSignUp.setOnClickListener { registerScreen() }
    }

    //Función que lleva al SignUpActivity al pulsar el botón "btn_sign_up"
    private fun registerScreen() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    //Función que lleve al LoginActivity al pulsar el botón "btn_login"
    private fun loginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}