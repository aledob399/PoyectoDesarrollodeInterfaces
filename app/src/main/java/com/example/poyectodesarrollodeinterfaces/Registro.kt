package com.example.poyectodesarrollodeinterfaces

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Registro : AppCompatActivity() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val perfilesCollection = firestore.collection("perfiles")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private var imagenSeleccionada: Uri? = null
    private var imagenEnFirebase: String? = null // Almacena la URL de la imagen actual en Firebase Storage

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        setup()
    }

    private fun setup() {
        val signUpButton = findViewById<Button>(R.id.registrarse)
        title = "Autentificacion"
        signUpButton.setOnClickListener {

            val usuarioEditText = findViewById<EditText>(R.id.usuario)
            val emailEditText = findViewById<EditText>(R.id.email)
            val passwordEditText = findViewById<EditText>(R.id.contraseña)
            val fotoUsuario = findViewById<ImageButton>(R.id.fotoPerfil)

            fotoUsuario.setOnClickListener {
                seleccionarImagen()
            }
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() && usuarioEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                            // Subir la imagen a Firebase Storage y guardar la URL en Firestore
                            subirImagenYGuardarPerfil(usuarioEditText.text.toString())
                        } else {
                            Toast.makeText(applicationContext, "Usuario no válido", Toast.LENGTH_SHORT).show()
                            showAlert()
                        }
                    }
            }
        }
    }

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

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

data class Perfil(
    val nombreUsuario: String = "",
    val imagen: String = ""
)
