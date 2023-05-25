package com.example.tfg_championship_studio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_championship_studio.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    object GlobalData{
        var emailKey = ""
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnSignUp.setOnClickListener { signUpUser() }
    }

    //Función que hace el registro de usuario con control de errores
    private fun signUpUser() {
        val empty = checkFieldsEmpty()
        val length = checkLength()
        val password = checkPassword()

        if (!empty) {
            showAlertEmpty()
        }else if (!length) {
            showAlertLength()
        }else if (!password){
            showAlertPassword()
        }else if (empty && length && password){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
            GlobalData.emailKey = binding.email.text.toString()
            val emptyMap = mapOf<String, Any>()

            val data = hashMapOf<String, Any>()
            data["Torneo"] = emptyMap

            userCollection.document(GlobalData.emailKey).set(data)
            iniciaEmparejamientos()
            showLogin()
        }else {
            showAlert()
        }
    }

    private fun checkFieldsEmpty(): Boolean {
        if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty() && binding.rePassword.text.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun checkPassword(): Boolean {
        if (binding.password.text.toString() == binding.rePassword.text.toString()) {
            return true
        }
        return false
    }

    private fun checkLength(): Boolean {
        if (binding.password.text.toString().length >= 6) {
            return true
        }
        return false
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Ha ocurrido un error al introducir los datos en la base de datos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertLength() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, la contraseña debe ser mayor a 6 caracteres")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, no puedes dejar un campo vacío")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertPassword() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, las contraseñas no coinciden")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showLogin(){
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    private fun iniciaEmparejamientos(){
        val documentRef = FirebaseFirestore.getInstance().collection("users").document(SignUpActivity.GlobalData.emailKey)
        val emparejamientosData = HashMap<String, Any>()
        documentRef.set(mapOf("Emparejamientos" to emparejamientosData), SetOptions.merge())
            .addOnSuccessListener {
                println("La colección 'Emparejamientos' se creó correctamente.")
            }
            .addOnFailureListener { e ->
                println("Error al crear la colección 'Emparejamientos': $e")
            }
    }
}