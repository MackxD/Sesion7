package com.example.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    // Inicializar FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Siempre que se instancie algo, va al inicio
        auth = FirebaseAuth.getInstance()

        loginAnonymus()

        //Inicializar la instancia para el database
        database = FirebaseDatabase.getInstance()

        getSeasons()

        setSeason(id = "8")





    }

    override fun onStart() {
        super.onStart()

        // Verificar si el usuario ya esta autenticado
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {

            if (currentUser.email != "") {
                //El usuario ya esta autenticado realizar una accion
                Toast.makeText(this, "Bienvenido: " + currentUser.email, Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(this, "Bienvenido anonimo", Toast.LENGTH_LONG).show()

            }

        } else {
            //El usuario no esta atenticado login
            login("jeik_360@outlook.com", "12355678")
        }

    }


    fun login(email: String, password: String) {
// Iniciar sesion con correo electronico y password

        val email = "jeik_360@outlook.com"
        val password = "12345678"

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    //Inicio de sesion exitoso
                    val user = auth.currentUser
                    //Hacer algo con el usuario autenticado
                    Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_LONG).show()
                } else {
                    //Error de inicio de sesion
                    Toast.makeText(this, "Error de inicio de sesion exitoso", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    fun loginAnonymus() {
        //Iniciar sesion de forma anonima

        auth.signInAnonymously()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    //Inicio de sesion anonimo exitoso
                    val user = auth.currentUser
                    //Hacer algo con el usuario autenticado
                    Toast.makeText(this, "Inicio anonimo exitoso", Toast.LENGTH_LONG).show()

                } else {
                    //Error en el inicio de sesion
                    Toast.makeText(this, "Error en el inicio de session", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Funcion para base de datos
    fun getSeasons() {
        val reference = database.getReference("seasons")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (seasonSnapshot in dataSnapshot.children) {
                    val season = seasonSnapshot.getValue(Season::class.java)
                    //Hacer algo con cada valo de la lista
                    Toast.makeText(
                        this@MainActivity,
                        season?.name + ": " + season?.descripcion + season?.estatus,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error al leer los datos ${databaseError.message}")
            }
        })
    }

    fun setSeason(id: String) {
        val reference = database.getReference("seasons")

        val season = Season("Seasión prueba2", "Descripción prueba", false)

        reference.child(id).setValue(season).addOnCompleteListener {
            Toast.makeText(this@MainActivity, "Se guardo la info", Toast.LENGTH_LONG).show()
        }

    }
}


