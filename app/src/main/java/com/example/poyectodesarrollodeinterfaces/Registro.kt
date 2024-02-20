package com.example.poyectodesarrollodeinterfaces

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Registro : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private var PICK_IMAGE_REQUEST = 1
    private var imagenSeleccionada: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        setup()
    }


    private fun setup() {
        val signUpButton = findViewById<Button>(R.id.registrarse)
        val logInButton = findViewById<Button>(R.id.acceder)
        val usuarioEditText = findViewById<EditText>(R.id.usuario)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.contraseña)
        val fotoPerfil = findViewById<ImageButton>(R.id.fotoPerfil)
        val toogleContra = findViewById<ImageView>(R.id.toogleContra)

        title = "Registro de Usuario"
        logInButton.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val nombreUsuario = usuarioEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (nombreUsuario.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && contraValida(password)) {

                val user = User(nombreUsuario, email, password)
                firestore.collection("usuarios").document(nombreUsuario)
                    .set(user)
                    .addOnSuccessListener {
                        if (imagenSeleccionada != null) {
                            subirImagen(nombreUsuario)
                        } else {
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                passwordEditText.error = "La contraseña debe tener al menos 8 caracteres, incluyendo al menos una letra mayúscula y un carácter especial."
            }
        }
        fotoPerfil.setOnClickListener {
            seleccionarImagen()
        }
        toogleContra.setOnClickListener {
            if (passwordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                toogleContra.setImageResource(R.drawable.ojo_cerrado)
            } else {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toogleContra.setImageResource(R.drawable.ojo)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }

    private fun contraValida(password: String): Boolean {
        if(password.length>=6){
            val pattern = "(?=.*[A-Z])(?=.*\\d).{8,}".toRegex()
            return pattern.matches(password)
        }else return false

    }
    private fun seleccionarImagen() {
        val intent = Intent().setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imagenSeleccionada = data.data
            val fotoUsuario = findViewById<ImageButton>(R.id.fotoPerfil)
            Glide.with(this).load(imagenSeleccionada).into(fotoUsuario)
        }
    }

    private fun subirImagen(nombreUsuario: String) {
        val storageReference = storageRef.child("imagenes/$nombreUsuario.jpg")
        storageReference.putFile(imagenSeleccionada!!)
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }
}
