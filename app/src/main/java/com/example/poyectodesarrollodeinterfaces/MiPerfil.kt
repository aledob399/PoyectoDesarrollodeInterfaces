package com.example.poyectodesarrollodeinterfaces

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MiPerfil : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val firestore = FirebaseFirestore.getInstance()
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val fotoPerfil = findViewById<ImageView>(R.id.imgPerfil)
        val usuarios = firestore.collection("usuarios").document(nombreUsuario!!)



        val btnAtras=findViewById<Button>(R.id.btnAtras)
        val usernameText=findViewById<TextView>(R.id.username)
        val passwordText=findViewById<TextView>(R.id.password)
        val emailText=findViewById<TextView>(R.id.email)




        if (nombreUsuario != null) {
            val storageReference = FirebaseStorage.getInstance().reference.child("imagenes/$nombreUsuario.jpg")
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(fotoPerfil)
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar la imagen de perfil", Toast.LENGTH_SHORT).show()
            }
        }



        usuarios.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val email = documentSnapshot.getString("email")
                    emailText.text="Email:$email"
                    val password = documentSnapshot.getString("password")
                    passwordText.text="Contraseña: $password"
                    usernameText.text="Usuario: $nombreUsuario"
                } else {
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar el usuario", Toast.LENGTH_SHORT).show()
            }

        btnAtras.setOnClickListener {
            val intent = Intent(this, PeliculasSeries::class.java)
            intent.putExtra("nombreUsuario",nombreUsuario)
            startActivity(intent)
        }


    }
}