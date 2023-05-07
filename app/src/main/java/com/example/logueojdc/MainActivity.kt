package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LogueoJDC)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btn_ingresar=findViewById<Button>(R.id.ingresar)
        btn_ingresar.setOnClickListener {
           // val email = findViewById<TextView>(R.id.email)
            //val password = "password"
            val intent: Intent = Intent(this, inicio:: class.java)
            startActivity(intent)



                }
        val btn_registro=findViewById<Button>(R.id.registrar)
        btn_registro.setOnClickListener {
            // val email = findViewById<TextView>(R.id.email)
            //val password = "password"
            val intent: Intent = Intent(this, RegistroUser:: class.java)
            startActivity(intent)



        }


    }
}