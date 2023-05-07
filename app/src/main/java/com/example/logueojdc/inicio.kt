package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val btn_regresar=findViewById<Button>(R.id.regresar)
        btn_regresar.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
        }

    }
}