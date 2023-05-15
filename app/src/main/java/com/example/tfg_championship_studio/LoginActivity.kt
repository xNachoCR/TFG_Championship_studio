package com.example.tfg_championship_studio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_championship_studio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener { loginUser() }

    }

    private fun loginUser() {
        SignUpActivity.GlobalData.emailKey = binding.email.text.toString()
        if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.email.text.toString(), binding.password.text.toString()
            ).addOnCompleteListener {
                if(it.isSuccessful){
                    showMain()
                } else{
                    showAlert()
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al autenticar usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain() {
        val loginIntent = Intent(this, MainActivity::class.java)
        startActivity(loginIntent)
    }
}