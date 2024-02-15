package com.example.poyectodesarrollodeinterfaces
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PeliculasSeries : AppCompatActivity() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val seriesCollection = firestore.collection("series")
    private val pelisCollection = firestore.collection("peliculas")
    private val usuarioCollection = firestore.collection("usuario")



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_series)
        val fotoPerfil = findViewById<ImageButton>(R.id.imgPerfil)
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val pelis: List<Pelicula> = cargarPeliculas()
        val series: List<Serie> = cargarSeries()
        mostrarImagenesPeliculas(pelis)
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
        fotoPerfil.setOnClickListener {
            val intent=Intent(this,MiPerfil::class.java)
            intent.putExtra("nombreUsuario",nombreUsuario)
            startActivity(intent)
        }



/*
        pelisCollection.get()
    .addOnSuccessListener { documents ->
        val batch = firestore.batch()

        for (document in documents) {
            batch.delete(document.reference)
        }

        batch.commit()
            .addOnSuccessListener {
                // Después de borrar el contenido de la colección, procede a agregar nuevas películas
                for (p in pelis) {
                    pelisCollection.add(p)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Película añadida correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Error al agregar la película
                            Toast.makeText(this, "Error al añadir la película", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Error al borrar el contenido de la colección
                Toast.makeText(this, "Error al borrar el contenido de la colección", Toast.LENGTH_SHORT).show()
            }
    }
    .addOnFailureListener { exception ->
        // Error al obtener los documentos de la colección
        Toast.makeText(this, "Error al obtener los documentos de la colección", Toast.LENGTH_SHORT).show()
    }


 */







/*
        seriesCollection.get()
    .addOnSuccessListener { documents ->
        val batch = firestore.batch()

        for (document in documents) {
            batch.delete(document.reference)
        }

        batch.commit()
            .addOnSuccessListener {
                // Después de borrar el contenido de la colección, procede a agregar nuevas películas
                for (s in series) {
                    seriesCollection.add(s)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Serie añadida correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Error al agregar la serie
                            Toast.makeText(this, "Error al añadir la película", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Error al borrar el contenido de la colección
                Toast.makeText(this, "Error al borrar el contenido de la colección", Toast.LENGTH_SHORT).show()
            }
    }
    .addOnFailureListener { exception ->
        // Error al obtener los documentos de la colección
        Toast.makeText(this, "Error al obtener los documentos de la colección", Toast.LENGTH_SHORT).show()
    }


 */




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
                    val imagen = document.getLong("imagen")?.toInt() ?: 0
                    val videoUrl = document.getString("videoUrl") ?: ""

                    val pelicula = Pelicula(titulo, sinopsis, imagen, videoUrl)
                    peliculasList.add(pelicula)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar la pelicula", Toast.LENGTH_SHORT).show()
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
                    val imagen = document.getLong("imagen")?.toInt() ?: 0
                    val videoUrl = document.getString("videoUrl") ?: ""

                    val serie = Serie(titulo, sinopsis, imagen, videoUrl)
                    seriesList.add(serie)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar la serie", Toast.LENGTH_SHORT).show()
            }


        return seriesList
    }
    // Función para crear un conjunto de películas
    private fun añadirPelis(): List<Pelicula> {
        val peliculas = mutableListOf<Pelicula>()

        // Agregar películas a la lista
        peliculas.add(Pelicula("Max payne", "Para resolver una serie de asesinatos en Nueva York se unen un detective de policía y un asesino, que serán perseguidos por la policía, la mafia y una corporación despiadada.", R.drawable.maxpayne, "https://www.youtube.com/watch?v=GklHaGfncJI"))
        peliculas.add(Pelicula("El hombre lobo", "Al regresar a su tierra ancestral, un hombre americano es mordido y posteriormente maldecido por un hombre lobo.", R.drawable.elhombrelobo, "https://www.youtube.com/watch?v=Wff_63MgPPI"))
        peliculas.add(Pelicula("John Wick (Otro día para matar)", "Un ex-sicario sale de su retiro para perseguir a los gángsters que mataron a su perro y le robaron el coche.", R.drawable.johnwick, "https://www.youtube.com/watch?v=TWRxFTiNTyU"))
        peliculas.add(Pelicula("Juego de armas", "Basada libremente en la historia real de dos jóvenes, David Packouz y Efraim Diveroli, que consiguieron un contrato de trescientos millones de dólares del Pentágono para armar a los aliados de Estados Unidos en Afganistán.", R.drawable.juegodearmas, "https://www.youtube.com/watch?v=C0eT-Vcovqc"))
        peliculas.add(Pelicula("Los odiosos ocho", "En pleno invierno de Wyoming, un cazarrecompensas y su prisionero encuentran refugio en una cabaña habitada actualmente por una colección de nefastos personajes.", R.drawable.losodiososocho, "https://www.youtube.com/watch?v=JxVRgBOL8jc"))
        peliculas.add(Pelicula("Seven", "Dos detectives, un novato y un veterano, dan caza a un asesino en serie que utiliza los siete pecados capitales como motivos.", R.drawable.seven, "https://www.youtube.com/watch?v=znmZoVkCjpI"))
        peliculas.add(Pelicula("El lobo de Wall Street", "Basada en la historia real de Jordan Belfort, desde su ascenso como acaudalado corredor de bolsa hasta su caída en manos del crimen, la corrupción y el gobierno federal.", R.drawable.ellobodewallstreet, ""))
        peliculas.add(Pelicula("Focus(II)", "En medio del último plan del veterano estafador Nicky, una mujer de su pasado ahora una consumada femme fatale aparece y da al traste con sus planes.", R.drawable.focus, "https://www.youtube.com/watch?v=DEMZSa0esCU"))
        peliculas.add(Pelicula("El corazon de acero", "Un canoso comandante de tanque toma decisiones difíciles mientras él y su tripulación se abren camino a través de Alemania en abril de 1945.", R.drawable.corazonesdehierro, "https://www.youtube.com/watch?v=yftjMJVN4dE"))


        return peliculas
    }





    private fun añadirSeries(): List<Serie> {
        val series = mutableListOf<Serie>()

        // Agregar series a la lista
        series.add(Serie("Breaking bad", "Un profesor de instituto diagnosticado con cáncer de pulmón empieza a manufacturar y vender metamfetamina para asegurar el futuro de su familia.", R.drawable.breakingbad, "https://www.youtube.com/watch?v=HhesaQXLuRY"))
        series.add(Serie("Hermanos de sangre", "La historia de la Easy Company de la División Aerotransportada 101 del Ejército de los Estados Unidos y su misión en la Segunda Guerra Mundial en Europa, desde la Operación Overlord hasta el Día V-J.", R.drawable.hermanosdesangre, "https://www.youtube.com/watch?v=aH06LWZs-Ys"))
        series.add(Serie("Juego de tronos", "Nueve familias nobles luchan por el control de las tierras de Poniente, mientras un antiguo enemigo regresa tras permanecer inactivo durante milenios.", R.drawable.juegodetronos, ""))
        series.add(Serie("Chernobyl", "Esta apasionante miniserie narra la impactante historia del peor accidente provocado por el hombre de la historia, siguiendo la tragedia desde el momento de la explosión a primera hora de la mañana hasta el caos y la pérdida de vidas en los días, semanas y meses siguientes.", R.drawable.chernobyl, ""))
        series.add(Serie("Sherlock", "El peculiar giro del icónico detective de Conan Doyle lo presenta como un \"sociópata de alto funcionamiento\" en el Londres actual. Le ayuda en sus investigaciones: John Watson, veterano de la guerra de Afganistán, que es presentado a Holmes por un conocido común.", R.drawable.sherlock, ""))
        series.add(Serie("Los Soprano", "El jefe de la mafia de Nueva Jersey Tony Soprano se enfrenta a problemas personales y profesionales en su vida familiar y empresarial que afectan a su estado mental, lo que le lleva a buscar ayuda psiquiátrica profesional.", R.drawable.lossoprano, ""))
        series.add(Serie("Rick y Morty", "Las fracturadas vidas domésticas de un científico loco nihilista y su ansioso nieto se complican aún más con sus desventuras interdimensionales.", R.drawable.rickymorty, ""))
        series.add(Serie("Friends", "Sigue la vida personal y profesional de seis amigos de veinte a treinta años que viven en el barrio de Manhattan, en Nueva York.", R.drawable.friends, ""))
        series.add(Serie("The Office", "Un falso documental sobre un grupo de oficinistas típicos, donde la jornada laboral consiste en choques de egos, comportamientos inapropiados y tedio.", R.drawable.theoffice, ""))
        series.add(Serie("Ataque a los titane", "Después de que su ciudad natal sea destruida y quede traumatizado, el joven Eren Jaeger jura limpiar la Tierra de los gigantescos Titanes humanoides que han llevado a la humanidad al borde de la extinción.", R.drawable.ataquealostitanes, ""))
        series.add(Serie("Stranger Things", "Cuando un niño desaparece, un pequeño pueblo descubre un misterio que implica experimentos secretos, terroríficas fuerzas sobrenaturales y una extraña niña.", R.drawable.strangerthings, ""))
        series.add(Serie("Peaky Blinders", "Una epopeya familiar de gángsters ambientada en la Inglaterra de 1900, centrada en una banda que cose cuchillas de afeitar en los picos de sus gorras, y en su feroz jefe Tommy Shelby.", R.drawable.peakybliders, ""))


        return series
    }








    private fun mostrarImagenesPeliculas(peliculas: List<Pelicula>) {
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        val imageContainer2 = findViewById<LinearLayout>(R.id.imageContainer2)
        imageContainer.removeAllViews()
        imageContainer2.removeAllViews()
        var pelisAleatorias=peliculas.shuffled()
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
        for (index in pelisAleatorias.indices.reversed()) {
            val pelicula = peliculas[index]
            val imageButton = ImageButton(this)
            val id = View.generateViewId()
            imageButton.id = id
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8.dpToPx(), 0, 8.dpToPx(), 0)
            }
            imageButton.setBackgroundResource(pelicula.imagen)
            imageButton.setOnClickListener {
                val intent = Intent(this, InfoPeliSerie::class.java)
                intent.putExtra("titulo", pelicula.titulo)
                intent.putExtra("sinopsis", pelicula.sinopsis)
                intent.putExtra("imagen", pelicula.imagen)
                intent.putExtra("videoUrl", pelicula.videoUrl)
                startActivity(intent)
            }
            imageContainer2.addView(imageButton)
        }
    }


    private fun mostrarImagenesSeries(series: List<Serie>) {
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        val imageContainer2 = findViewById<LinearLayout>(R.id.imageContainer2)
        imageContainer.removeAllViews()
        var seriesAleatorias=series.shuffled()
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
        for (index in seriesAleatorias.indices.reversed()) {
            val serie = series[index]
            val imageButton = ImageButton(this)
            val id = View.generateViewId()
            imageButton.id = id
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8.dpToPx(), 0, 8.dpToPx(), 0)
            }
            imageButton.setBackgroundResource(serie.imagen)
            imageButton.setOnClickListener {
                val intent = Intent(this, InfoPeliSerie::class.java)
                intent.putExtra("titulo", serie.titulo)
                intent.putExtra("sinopsis", serie.sinopsis)
                intent.putExtra("imagen", serie.imagen)
                intent.putExtra("videoUrl", serie.videoUrl)
                startActivity(intent)
            }
            imageContainer2.addView(imageButton)
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
    val imagen: Int = 0,
    val videoUrl: String = ""
)



data class Serie(
    val titulo: String = "",
    val sinopsis: String = "",
    val imagen: Int = 0,
    val videoUrl: String = ""
)
