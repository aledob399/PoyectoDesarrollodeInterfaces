package com.example.poyectodesarrollodeinterfaces

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        firestore = FirebaseFirestore.getInstance()

        setup()
    }

    private fun setup() {
        val signUpButton = findViewById<Button>(R.id.registrarse)
        val usuarioEditText = findViewById<EditText>(R.id.usuario)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.contraseña)

        title = "Registro de Usuario"

        signUpButton.setOnClickListener {
            val nombreUsuario = usuarioEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (nombreUsuario.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Guardar los datos del usuario en Firestore
                val user = User(nombreUsuario, email, password)
                firestore.collection("usuarios").document(nombreUsuario)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        // Redirigir al usuario a la actividad de inicio de sesión
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }



/*

    private fun seleccionarImagen() {
        val intent = Intent().setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun subirImagenYGuardarPerfil(nombreUsuario: String) {
        if (imagenSeleccionada != null) {
            // Generar un nombre único para la nueva imagen
            val imageName = "image_${UUID.randomUUID()}.jpg"
            // Crear una referencia al nuevo archivo en Firebase Storage
            val newImageRef = storageRef.child("images/$imageName")

            // Subir la nueva imagen
            newImageRef.putFile(imagenSeleccionada!!)
                .addOnSuccessListener {
                    // Obtener la URL de descarga de la nueva imagen
                    newImageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Actualizar la URL de la imagen en Firebase Storage
                        imagenEnFirebase = downloadUri.toString()

                        // Crear un objeto Perfil con la información actualizada
                        val perfil = Perfil(nombreUsuario, imagenEnFirebase!!)

                        // Guardar el objeto Perfil en Firestore
                        perfilesCollection.add(perfil)


                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar el error al subir la nueva imagen a Firebase Storage
                    showAlert()
                }
        } else {
            // No se ha seleccionado ninguna nueva imagen
            // Puedes manejar esta situación según tus requisitos
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imagenSeleccionada = data.data
            val fotoUsuario = findViewById<ImageButton>(R.id.fotoPerfil)
            Glide.with(this).load(imagenSeleccionada).into(fotoUsuario)
        }
    }
    */
 */

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


