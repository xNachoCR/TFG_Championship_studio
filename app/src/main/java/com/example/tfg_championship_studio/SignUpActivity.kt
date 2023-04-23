package com.example.tfg_championship_studio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_championship_studio.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnSignUp.setOnClickListener { signUpUser() }
    }

    //Función que hace el registro de usuario con control de errores
    private fun signUpUser() {
        if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty() && binding.rePassword.text.isNotEmpty()){
            if (binding.password.text.toString() == binding.rePassword.text.toString() && binding.password.text.toString().length >= 6){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(binding.email.text.toString(),
                        binding.password.text.toString()).addOnCompleteListener {

                        if(it.isSuccessful){
                            showLogin()
                        } else{
                            showAlert()
                        }
                    }
            }
        }
    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al registrar usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showLogin(){
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}