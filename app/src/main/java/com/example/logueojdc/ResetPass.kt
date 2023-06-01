package com.example.logueojdc


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

/*Autor: Jose Esteban Bueno Sierra
Fecha:13/05/2023
Comentario:se realiza clase para el reestablecimiento de contraseña mediante el uso de firebase en donde se le envia el correo y automaticamente
envia al correo la notificacion de cambio de contraseña, tambien se agrega boton de regreso al layout para devolver al usuario a la pagina de logueo

*/
class ResetPass : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_reset_pass)
        val home = findViewById<Button>(R.id.returnHome1)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        val btnreset=findViewById<Button>(R.id.Btn_reset)
        btnreset.setOnClickListener {
            val email2=findViewById<TextView>(R.id.emailreset)
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
}