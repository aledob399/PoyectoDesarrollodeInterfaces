package com.example.poyectodesarrollodeinterfaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
        val imageShowPassword = findViewById<ImageView>(R.id.imageShowPassword)

        imageShowPassword.setOnClickListener {
            val editTextContraseña = findViewById<EditText>(R.id.contraseña)
            if (editTextContraseña.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                editTextContraseña.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT

                imageShowPassword.setImageResource(R.drawable.ojo)
            } else {
                editTextContraseña.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageShowPassword.setImageResource(R.drawable.ojo_cerrado)
            }
            editTextContraseña.setSelection(editTextContraseña.text.length)
        }
        title = "Autenticación"
        signInButton.setOnClickListener {
            val intent=Intent(this,Registro::class.java)
            startActivity(intent)
        }
        logInButton.setOnClickListener {
            val nombreUsuario = usuarioEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (nombreUsuario.isNotEmpty() && password.isNotEmpty()) {
                if (isValidPassword(password)) {
                    firestore.collection("usuarios").document(nombreUsuario).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val user = documentSnapshot.toObject(User::class.java)
                                if (user != null && user.password == password) {
                                    // Iniciar sesión exitosamente
                                    val intent = Intent(this, PeliculasSeries::class.java)
                                    intent.putExtra("nombreUsuario",nombreUsuario)
                                    startActivity(intent)
                                    // Mostrar los datos del usuario en un Toast
                                    Toast.makeText(applicationContext, "Nombre de usuario: ${user.nombreUsuario}\nEmail: ${user.email}\nContraseña: ${user.password}", Toast.LENGTH_LONG).show()
                                } else {
                                    passwordEditText.error = "Nombre de usuario o contraseña incorrectos."
                                }
                            } else {
                                passwordEditText.error = "Usuario no encontrado."
                            }
                        }
                        .addOnFailureListener { e ->
                            // Manejar errores al acceder a Firestore
                            passwordEditText.error = "Error al acceder a Firestore: ${e.message}"
                        }
                } else {
                    passwordEditText.error = "La contraseña debe tener al menos 8 caracteres, incluyendo al menos una letra mayúscula y un carácter especial."
                }
            } else {
                passwordEditText.error = "Por favor, introduce un nombre de usuario y una contraseña."
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        // Definir el nuevo patrón de la contraseña
        val pattern = "(?=.*[A-Z])(?=.*\\d).{8,}".toRegex()
        // Verificar si la contraseña coincide con el nuevo patrón
        return pattern.matches(password)
    }
}

data class User(
    val nombreUsuario: String = "",
    val email: String = "",
    val password: String = ""
)
