package com.example.poyectodesarrollodeinterfaces


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signUpButton=findViewById<Button>(R.id.acceder)
        val analytics: FirebaseAnalytics =FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        bundle.putString("mensaje","Integracion de firebase completa")
        analytics.logEvent("InitScreep",bundle)

        setup()


    }
    private fun setup(){



        val signUpButton=findViewById<Button>(R.id.registrarse)
        val logInButton=findViewById<Button>(R.id.acceder)
        val usuarioEditText=findViewById<EditText>(R.id.usuario)
        val passwordEditText=findViewById<EditText>(R.id.contraseña)

        title="Autentificacion"
        signUpButton.setOnClickListener{
            val intent=Intent(this,Registro::class.java)
            startActivity(intent)
        }
        logInButton.setOnClickListener{

            if (usuarioEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                val id = FirebaseAuth.getInstance().currentUser?.providerId

                if (id != null) {
                    val email=FirebaseDatabase.getInstance().reference.child("Usuario").child(id).child("email").key
                    if (email != null) {

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,passwordEditText.text.toString()).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent=Intent(this,PeliculasSeries::class.java)
                                startActivity(intent)

                            }else{
                                showAlert()
                            }
                        }
                    }
                }

            }
        }


    }
    private fun showAlert(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentificando el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
}