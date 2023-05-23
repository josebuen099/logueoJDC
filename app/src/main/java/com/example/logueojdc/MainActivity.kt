package com.example.logueojdc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
        val btnreset=findViewById<Button>(R.id.reset_password_button)
        btnreset.setOnClickListener {
            val intent = Intent(this, ResetPass::class.java)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()
        val btn_registrar=findViewById<Button>(R.id.registrar)
        btn_registrar.setOnClickListener {
            val intent = Intent(this, RegistroUser::class.java)
            startActivity(intent)
            finish()
        }

        //boton para resetear los campos de email y contraseña
        val btn_borrarcontenido=findViewById<ImageButton>(R.id.clear)
        btn_borrarcontenido.setOnClickListener {
            val email2 = findViewById<TextView>(R.id.email)
            val password2 = findViewById<TextView>(R.id.password)
           email2.setText("")
            password2.setText("")


        }
// boton para ingresar a firebase auten
        val btn_ingresar=findViewById<Button>(R.id.ingresar)
        btn_ingresar.setOnClickListener {
            val email2 = findViewById<TextView>(R.id.email)
            val password2 = findViewById<TextView>(R.id.password)
            val email = email2.text.toString()
            val password = password2.text.toString()
            // Patrón para identificar que el correo electrónico cumpla con el estandar de ser Institucional JDC
            val patrondeEmail = "[a-zA-Z0-9._-]+@[jdc]+\\.+[edu]+\\.+[co]+"
           // val patrondeEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+"
//condicion para revisar que email y contraseña no sean vacios

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // El inicio de sesión fue exitoso
                            val intent = Intent(this, inicio::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            if (email.matches(patrondeEmail.toRegex())){
                                Toast.makeText(this, "El inicio de sesión falló. Verifique los datos.", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, "El email ingresado no cumple con las características.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                if (email.isEmpty()){
                    Toast.makeText(this, "Por favor ingrese su correo electrónico.", Toast.LENGTH_SHORT).show()
                }else {
                    if (password.isEmpty()){
                        Toast.makeText(this, "Por favor ingrese su contraseña.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Por favor ingrese su correo electrónico y contraseña.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

// mantiene la seccion activa
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

}
