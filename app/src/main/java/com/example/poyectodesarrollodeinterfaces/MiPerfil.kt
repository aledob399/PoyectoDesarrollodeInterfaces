package com.example.poyectodesarrollodeinterfaces
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MiPerfil : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var nombreUsuario: String
    private var imagenSeleccionada: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

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
        val imgPerfil = findViewById<ImageButton>(R.id.imgPerfil)


        val storageReference = FirebaseStorage.getInstance().reference.child("imagenes/$nombreUsuario.jpg")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .into(imgPerfil)
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al cargar la imagen de perfil", Toast.LENGTH_SHORT).show()
        }

        firestore.collection("usuarios").document(nombreUsuario)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val email = document.getString("email")
                    val username = document.getString("nombreUsuario")
                    val password = document.getString("password")

                    // Mostrar los datos actuales del usuario en los EditText
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

        btnAtras.setOnClickListener {
            val nuevoEmail = editEmail.text.toString().trim()
            val nuevoUsername = editUsername.text.toString().trim()
            val nuevaPassword = editPassword.text.toString().trim()

            // Verificar que los nuevos datos no estén vacíos
            if (nuevoEmail.isNotEmpty() && nuevoUsername.isNotEmpty() && nuevaPassword.isNotEmpty()) {
                // Verificar si la contraseña es adecuada
                if (isValidPassword(nuevaPassword)) {
                    // Actualizar los datos del usuario en Firebase
                    val user = User(nuevoUsername, nuevoEmail, nuevaPassword)
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
                                    // Subir la nueva imagen de perfil si se seleccionó una
                                    if (imagenSeleccionada != null) {
                                        subirImagenPerfil(user.nombreUsuario)
                                    } else {
                                        Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
                                        // Actualizar el nombre de usuario en la actividad
                                        nombreUsuario = user.nombreUsuario
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(this, "Error al eliminar el documento anterior: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error al actualizar los datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "La contraseña no es adecuada.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, PeliculasSeries::class.java)
            intent.putExtra("nombreUsuario", nombreUsuario)
            startActivity(intent)
            finish()
        }

        imgPerfil.setOnClickListener {
            seleccionarImagen()
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

    private fun isValidPassword(password: String): Boolean {
        // Definir el nuevo patrón de la contraseña
        if(password.length>=6){
            val pattern = "(?=.*[A-Z])(?=.*\\d).{8,}".toRegex()
            // Verificar si la contraseña coincide con el nuevo patrón
            return pattern.matches(password)
        }else return false

    }

    private fun seleccionarImagen() {
        val intent = Intent().setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imagenSeleccionada = data.data
            val imgPerfil = findViewById<ImageButton>(R.id.imgPerfil)
            Glide.with(this).load(imagenSeleccionada).into(imgPerfil)
        }
    }

    private fun subirImagenPerfil(nombreUsuario: String) {
        val storageReference = FirebaseStorage.getInstance().reference.child("imagenes/$nombreUsuario.jpg")
        storageReference.putFile(imagenSeleccionada!!)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
                // Actualizar el nombre de usuario en la actividad
                this.nombreUsuario = nombreUsuario
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen de perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
