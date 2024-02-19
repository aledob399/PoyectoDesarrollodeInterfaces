package com.example.poyectodesarrollodeinterfaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MiPerfil : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var nombreUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        firestore = FirebaseFirestore.getInstance()
        nombreUsuario = intent.getStringExtra("nombreUsuario")!!

        val btnAtras = findViewById<Button>(R.id.btnAtras)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val imageShowPassword = findViewById<ImageView>(R.id.imageShowPassword)

        // Cargar los datos actuales del usuario al iniciar la actividad
        cargarDatosUsuario()

        btnAtras.setOnClickListener {
            // Obtener los nuevos datos del usuario de los EditText
            val nuevoEmail = editEmail.text.toString().trim()
            val nuevoUsername = editUsername.text.toString().trim()
            val nuevaPassword = editPassword.text.toString().trim()

            // Verificar que los nuevos datos no estén vacíos
            if (nuevoEmail.isNotEmpty() && nuevoUsername.isNotEmpty() && nuevaPassword.isNotEmpty()) {
                // Actualizar los datos del usuario en Firebase
                val user = User(nuevoUsername, nuevoEmail, nuevaPassword)
                actualizarDatosUsuario(user)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        imageShowPassword.setOnClickListener {
            // Cambiar el tipo de entrada de la contraseña para mostrar u ocultar
            if (editPassword.inputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                editPassword.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD or android.text.InputType.TYPE_CLASS_TEXT
                imageShowPassword.setImageResource(R.drawable.ojo_cerrado)
            } else {
                editPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageShowPassword.setImageResource(R.drawable.ojo)
            }
            // Mover el cursor al final del texto
            editPassword.setSelection(editPassword.text.length)
        }
    }

    private fun cargarDatosUsuario() {
        firestore.collection("usuarios").document(nombreUsuario)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val email = document.getString("email")
                    val username = document.getString("nombreUsuario")
                    val password = document.getString("password")

                    // Mostrar los datos actuales del usuario en los EditText
                    val editEmail = findViewById<EditText>(R.id.editEmail)
                    val editUsername = findViewById<EditText>(R.id.editUsername)
                    val editPassword = findViewById<EditText>(R.id.editPassword)

                    editEmail.setText(email)
                    editUsername.setText(username)
                    editPassword.setText(password)
                } else {
                    Toast.makeText(this, "No se encontraron datos del usuario.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar los datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarDatosUsuario(user: User) {
        val datosActualizados = hashMapOf(
            "email" to user.email,
            "nombreUsuario" to user.nombreUsuario,
            "password" to user.password
        )

        // Actualizar los datos del usuario en Firebase y cambiar el nombre del documento
        firestore.collection("usuarios").document(user.nombreUsuario)
            .set(datosActualizados)
            .addOnSuccessListener {
                // Eliminar el documento anterior del usuario
                firestore.collection("usuarios").document(nombreUsuario).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
                        // Actualizar el nombre de usuario en la actividad
                        nombreUsuario = user.nombreUsuario
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al eliminar el documento anterior: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al actualizar los datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
