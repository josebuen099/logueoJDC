package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class inicio : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        auth = FirebaseAuth.getInstance()


        val btn_regresar=findViewById<Button>(R.id.regresar)
        btn_regresar.setOnClickListener {
            auth.signOut()
            val intent: Intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
            finish()

        }

    }
}