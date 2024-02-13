package com.example.poyectodesarrollodeinterfaces
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class PeliculasSeries : AppCompatActivity() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val seriesCollection = firestore.collection("series")
    private val pelisCollection = firestore.collection("peliculas")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_series)

        // Crear un conjunto de películas

        val pelis: List<Pelicula> = añadirPelis()
        for (p in pelis) {
            pelisCollection.add(p)
                .addOnSuccessListener {
                    // Película agregada exitosamente
                }
                .addOnFailureListener { e ->
                    // Error al agregar la película
                    Toast.makeText(this, "Error al añadir la pelicula", Toast.LENGTH_SHORT).show()
                }
            val user = User(
                "prueba", "hola@gmail.com"," holaaaa")
            firestore.collection("usuarios").add(user)
            val peli=Peli("Spiderman 2")
            firestore.collection("peli").add(peli)
        }
        // Agregar las películas a la base de datos Firestore
       // agregarPelicula(pelis[0])

        // Crear un conjunto de series
        val series: List<Serie> = añadirSeries()
        for (s in series) {
            seriesCollection.add(s)
                .addOnSuccessListener {
                    // Serie agregada exitosamente
                }
                .addOnFailureListener { e ->
                    // Error al agregar la serie
                    Toast.makeText(this, "Error al añadir la serie", Toast.LENGTH_SHORT).show()
                }
        }
        // Agregar las series a la base de datos Firestore
      //  agregarSerie(series[0])

        val btnPelis = findViewById<Button>(R.id.btnPeliculas)
        val btnSeries = findViewById<Button>(R.id.btnSeries)

        btnPelis.setOnClickListener {
            mostrarImagenesPeliculas(pelis)
        }

        btnSeries.setOnClickListener {
            mostrarImagenesSeries(series)
        }
    }
    fun cargarPeliculas(): List<Pelicula> {
        val peliculasList = mutableListOf<Pelicula>()

        firestore.collection("peliculas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val titulo = document.getString("titulo") ?: ""
                    val sinopsis = document.getString("sinopsis") ?: ""
                    val imagenResId = document.getLong("imagen")?.toInt() ?: 0
                    val videoUrl = document.getString("videoUrl") ?: ""

                    val pelicula = Pelicula(titulo, sinopsis, R.drawable.breakingbad, videoUrl)
                    peliculasList.add(pelicula)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error de carga de datos
            }

        return peliculasList
    }

    // Función para cargar todas las series de Firestore y guardarlas en una lista
    fun cargarSeries(): List<Serie> {
        val seriesList = mutableListOf<Serie>()

        firestore.collection("series")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val titulo = document.getString("titulo") ?: ""
                    val sinopsis = document.getString("sinopsis") ?: ""
                    val imagenResId = document.getLong("imagen")?.toInt() ?: 0
                    val videoUrl = document.getString("videoUrl") ?: ""

                    val serie = Serie(titulo, sinopsis, imagenResId, videoUrl)
                    seriesList.add(serie)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error de carga de datos
            }

        return seriesList
    }
    // Función para crear un conjunto de películas
    private fun añadirPelis(): List<Pelicula> {
        val peliculas = mutableListOf<Pelicula>()

        // Agregar películas a la lista
        peliculas.add(Pelicula("Max payne", "Para resolver una serie de asesinatos en Nueva York se unen un detective de policía y un asesino, que serán perseguidos por la policía, la mafia y una corporación despiadada.", R.drawable.maxpayne, "https://www.youtube.com/watch?v=GklHaGfncJI"))
        peliculas.add(Pelicula("El hombre lobo", "Al regresar a su tierra ancestral, un hombre americano es mordido y posteriormente maldecido por un hombre lobo.", R.drawable.elhombrelobo, ""))
        peliculas.add(Pelicula("John Wick (Otro día para matar)", "Un ex-sicario sale de su retiro para perseguir a los gángsters que mataron a su perro y le robaron el coche.", R.drawable.johnwick, ""))
        peliculas.add(Pelicula("Juego de armas", "Basada libremente en la historia real de dos jóvenes, David Packouz y Efraim Diveroli, que consiguieron un contrato de trescientos millones de dólares del Pentágono para armar a los aliados de Estados Unidos en Afganistán.", R.drawable.juegodearmas, ""))
        peliculas.add(Pelicula("Los odiosos ocho", "En pleno invierno de Wyoming, un cazarrecompensas y su prisionero encuentran refugio en una cabaña habitada actualmente por una colección de nefastos personajes.", R.drawable.losodiososocho, ""))
        peliculas.add(Pelicula("Seven", "Dos detectives, un novato y un veterano, dan caza a un asesino en serie que utiliza los siete pecados capitales como motivos.", R.drawable.seven, ""))
        peliculas.add(Pelicula("El lobo de Wall Street", "Basada en la historia real de Jordan Belfort, desde su ascenso como acaudalado corredor de bolsa hasta su caída en manos del crimen, la corrupción y el gobierno federal.", R.drawable.ellobodewallstreet, ""))
        peliculas.add(Pelicula("Focus(II)", "En medio del último plan del veterano estafador Nicky, una mujer de su pasado ahora una consumada femme fatale aparece y da al traste con sus planes.", R.drawable.focus, ""))
        peliculas.add(Pelicula("El corazon de acero", "Un canoso comandante de tanque toma decisiones difíciles mientras él y su tripulación se abren camino a través de Alemania en abril de 1945.", R.drawable.corazonesdehierro, ""))
        // Agrega más películas según sea necesario

        return peliculas
    }

    // Función para agregar películas a Firestore


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
    fun agregarPelicula(pelicula: Pelicula) {
        firestore.collection("peliculas")
            .document(pelicula.titulo.toString())
            .set(pelicula)
            .addOnSuccessListener { documentReference ->
                // La película se agregó correctamente a Firestore
                Toast.makeText(this, "Película agregada correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Error al agregar la película a Firestore
                Toast.makeText(this, "Error al agregar la película", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para agregar una serie a Firestore
    fun agregarSerie(serie: Serie) {
        firestore.collection("series")
            .document(serie.titulo)
            .set(serie)
            .addOnSuccessListener { documentReference ->
                // La serie se agregó correctamente a Firestore
                Toast.makeText(this, "Serie agregada correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Error al agregar la serie a Firestore
                Toast.makeText(this, "Error al agregar la serie", Toast.LENGTH_SHORT).show()
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
            imageButton.setBackgroundResource(pelicula.imagen)
            imageButton.setOnClickListener {
                val intent=Intent(this,InfoPeliSerie::class.java)
                intent.putExtra("titulo",pelicula.titulo)
                intent.putExtra("sinopsis",pelicula.sinopsis)
                intent.putExtra("imagen",pelicula.imagen)
                intent.putExtra("videoUrl",pelicula.videoUrl)
                startActivity(intent)
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
            imageButton.setBackgroundResource(serie.imagen)
            imageButton.setOnClickListener {
                val intent=Intent(this,InfoPeliSerie::class.java)
                intent.putExtra("titulo",serie.titulo)
                intent.putExtra("sinopsis",serie.sinopsis)
                intent.putExtra("imagen",serie.imagen)
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
    val imagen: Int = 0, // Identificador de recurso de la imagen
    val videoUrl: String = ""
)
data class Peli(
    val nombre: String = "",

)

data class Serie(
    val titulo: String = "",
    val sinopsis: String = "",
    val imagen: Int = 0, // Identificador de recurso de la imagen
    val videoUrl: String = ""
)
