package com.example.poyectodesarrollodeinterfaces
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PeliculasSeries : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_series)

        // Crear un conjunto de películas
        val pelis: List<Pelicula> = añadirPelis()

        // Agregar las películas a la base de datos Firestore
        agregarPeliculasAFirestore(pelis)

        // Crear un conjunto de series
        val series: List<Serie> = añadirSeries()

        // Agregar las series a la base de datos Firestore
        agregarSeriesAFirestore(series)

        val btnPelis = findViewById<Button>(R.id.btnPeliculas)
        val btnSeries = findViewById<Button>(R.id.btnSeries)

        btnPelis.setOnClickListener {
            mostrarImagenesPeliculas(pelis)
        }

        btnSeries.setOnClickListener {
            mostrarImagenesSeries(series)
        }
    }

    // Función para crear un conjunto de películas
    private fun añadirPelis(): List<Pelicula> {
        val peliculas = mutableListOf<Pelicula>()

        // Agregar películas a la lista
        peliculas.add(Pelicula("Max payne", "Para resolver una serie de asesinatos en Nueva York se unen un detective de policía y un asesino, que serán perseguidos por la policía, la mafia y una corporación despiadada.", R.drawable.maxpayne, "URL Video Película 1"))
        peliculas.add(Pelicula("El hombre lobo", "Al regresar a su tierra ancestral, un hombre americano es mordido y posteriormente maldecido por un hombre lobo.", R.drawable.elhombrelobo, "URL Video Película 2"))
        // Agrega más películas según sea necesario

        return peliculas
    }

    // Función para agregar películas a Firestore
    private fun agregarPeliculasAFirestore(peliculas: List<Pelicula>) {
        for (pelicula in peliculas) {
            firestore.collection("peliculas")
                .add(pelicula)
                .addOnSuccessListener { documentReference ->
                    // La película se agregó correctamente a Firestore
                }
                .addOnFailureListener { e ->
                    // Error al agregar la película a Firestore
                }
        }
    }

    // Función para crear un conjunto de series
    private fun añadirSeries(): List<Serie> {
        val series = mutableListOf<Serie>()

        // Agregar series a la lista
        series.add(Serie("Breaking bad", "Un profesor de instituto diagnosticado con cáncer de pulmón empieza a manufacturar y vender metamfetamina para asegurar el futuro de su familia.", R.drawable.breakingbad, "URL Video Serie 1"))
        series.add(Serie("Hermanos de sangre", "La historia de la Easy Company de la División Aerotransportada 101 del Ejército de los Estados Unidos y su misión en la Segunda Guerra Mundial en Europa, desde la Operación Overlord hasta el Día V-J.", R.drawable.hermanosdesangre, "URL Video Serie 2"))
        // Agrega más series según sea necesario

        return series
    }

    // Función para agregar series a Firestore
    private fun agregarSeriesAFirestore(series: List<Serie>) {
        for (serie in series) {
            firestore.collection("series")
                .add(serie)
                .addOnSuccessListener { documentReference ->
                    // La serie se agregó correctamente a Firestore
                }
                .addOnFailureListener { e ->
                    // Error al agregar la serie a Firestore
                }
        }
    }

    // Función para mostrar las imágenes de películas en el HorizontalScrollView
    private fun mostrarImagenesPeliculas(peliculas: List<Pelicula>) {
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        imageContainer.removeAllViews()

        for ((index, pelicula) in peliculas.withIndex()) {
            val imageButton = ImageButton(this)
            val id = View.generateViewId() // Genera un ID único para el ImageButton
            imageButton.id = id
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8.dpToPx(), 0, 8.dpToPx(), 0)
            }
            imageButton.setBackgroundResource(pelicula.imagenResId)
            imageButton.setOnClickListener {
                intent.putExtra("titulo",pelicula.titulo)
                intent.putExtra("sinopsis",pelicula.sinopsis)
                intent.putExtra("imagen",pelicula.imagenResId)
                intent.putExtra("videoUrl",pelicula.videoUrl)
            }
            imageContainer.addView(imageButton)
        }
    }

    // Función para mostrar las imágenes de series en el HorizontalScrollView
    private fun mostrarImagenesSeries(series: List<Serie>) {
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        imageContainer.removeAllViews()

        for ((index, serie) in series.withIndex()) {
            val imageButton = ImageButton(this)
            val id = View.generateViewId() // Genera un ID único para el ImageButton
            imageButton.id = id
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8.dpToPx(), 0, 8.dpToPx(), 0)
            }
            imageButton.setBackgroundResource(serie.imagenResId)
            imageButton.setOnClickListener {
                val intent=Intent(this,InfoPeliSerie::class.java)
                intent.putExtra("titulo",serie.titulo)
                intent.putExtra("sinopsis",serie.sinopsis)
                intent.putExtra("imagen",serie.imagenResId)
                intent.putExtra("videoUrl",serie.videoUrl)
                startActivity(intent)
            }
            imageContainer.addView(imageButton)
        }
    }

    // Función de extensión para convertir dp a píxeles
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}

data class Pelicula(
    val titulo: String = "",
    val sinopsis: String = "",
    val imagenResId: Int = 0, // Identificador de recurso de la imagen
    val videoUrl: String = ""
)

data class Serie(
    val titulo: String = "",
    val sinopsis: String = "",
    val imagenResId: Int = 0, // Identificador de recurso de la imagen
    val videoUrl: String = ""
)
