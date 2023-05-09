package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.net.PasswordAuthentication

class RegistroUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_user)
        auth = FirebaseAuth.getInstance()

        val home = findViewById<Button>(R.id.returnHome)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        val registro = findViewById<Button>(R.id.registro)
        registro.setOnClickListener {
            val email2 = findViewById<TextView>(R.id.email2)
            val email = email2.text.toString()
            val password2 = findViewById<TextView>(R.id.password2)
            val password = password2.text.toString()
            val confirm_pas = findViewById<TextView>(R.id.confirm_pass)
            val confirm_password = confirm_pas.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirm_password.isNotEmpty()) {
                if (password == confirm_password) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // El usuario se creó con éxito
                                Toast.makeText(
                                    this,
                                    "El usuario se creó con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)

                            } else {
                                // La creación del usuario falló
                                Toast.makeText(
                                    this,
                                    "La creación del usuario falló",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Por favor ingrese su correo electrónico y contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}