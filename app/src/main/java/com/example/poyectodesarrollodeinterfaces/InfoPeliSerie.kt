package com.example.poyectodesarrollodeinterfaces

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class InfoPeliSerie : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_peli_serie)
        val imagen=intent.getIntExtra("imagen",0)
        val titulo=intent.getStringExtra("titulo")
        val sinopsis=intent.getStringExtra("sinopsis")
        val url=intent.getStringExtra("videoUrl")
        val btnAtras = findViewById<Button>(R.id.atras)
        val sinopsisTextView = findViewById<TextView>(R.id.sinopsis)
        val tituloTextView = findViewById<TextView>(R.id.titulo)
        val imagenImageView=findViewById<ImageView>(R.id.imagen)
        val videoUrlImageButton=findViewById<ImageButton>(R.id.videoUrl)

        btnAtras.setOnClickListener {
            onBackPressed()
        }
        sinopsisTextView.text=sinopsis
        tituloTextView.text=titulo
        imagenImageView.setImageResource(imagen)

    }
}