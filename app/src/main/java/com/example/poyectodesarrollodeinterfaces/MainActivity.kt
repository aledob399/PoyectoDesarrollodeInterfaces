package com.example.poyectodesarrollodeinterfaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setup()
    }

    private fun setup() {
        val logInButton = findViewById<Button>(R.id.acceder)
        val signInButton = findViewById<Button>(R.id.registrarse)
        val usuarioEditText = findViewById<EditText>(R.id.usuario)
        val passwordEditText = findViewById<EditText>(R.id.contraseña)

        title = "Autenticación"
        signInButton.setOnClickListener {
            val intent=Intent(this,Registro::class.java)
            startActivity(intent)
        }
        logInButton.setOnClickListener {
            val username = usuarioEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Verificar las credenciales del usuario en Firestore
                firestore.collection("usuarios").document(username).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val user = documentSnapshot.toObject(User::class.java)
                            if (user != null && user.password == password) {

                                // Iniciar sesión exitosamente
                                val intent = Intent(this@MainActivity, PeliculasSeries::class.java)
                                startActivity(intent)
                                // Mostrar los datos del usuario en un Toast
                                Toast.makeText(applicationContext, "Nombre de usuario: ${user.nombreUsuario}\nEmail: ${user.email}\nContraseña: ${user.password}", Toast.LENGTH_LONG).show()
                            } else {
                                showAlert()
                            }
                        } else {
                            showAlert()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al acceder a Firestore
                        showAlert()
                    }
            }
        }
    }

    private fun showAlert(){
        // Método para mostrar un diálogo de alerta en caso de error
    }
}

data class User(
    val nombreUsuario: String = "",
    val email: String = "",
    val password: String = ""
)
