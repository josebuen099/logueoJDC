package com.example.logueojdc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // tema layout para crear el splash de entrada
        setTheme(R.style.Theme_LogueoJDC)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val btn_registrar=findViewById<Button>(R.id.registrar)
        btn_registrar.setOnClickListener {
            val intent = Intent(this, RegistroUser::class.java)
            startActivity(intent)
            finish()
        }


        val btn_ingresar=findViewById<Button>(R.id.ingresar)
        btn_ingresar.setOnClickListener {
            val email2 = findViewById<TextView>(R.id.email)
            val password2 = findViewById<TextView>(R.id.password)
            val email = email2.text.toString()
            val password = password2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // El inicio de sesión fue exitoso
                            val intent = Intent(this, inicio::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // El inicio de sesión falló
                            Toast.makeText(this, "El inicio de sesión falló", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor ingrese su correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // El usuario ha iniciado sesión con éxito
                val intent = Intent(this, inicio::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    fun resetPassword(view: View) {
        val email2=findViewById<TextView>(R.id.email2)
        val email = email2.text.toString()

        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se ha enviado un correo electrónico para restablecer la contraseña",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "La recuperación de contraseña falló",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Ingrese su correo electrónico para solicitar la recuperación de contraseña",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
