package com.example.logueojdc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import java.util.*

class inicio : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db= FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        auth = FirebaseAuth.getInstance()




        val btn_regresar=findViewById<ImageButton>(R.id.regresar)
        btn_regresar.setOnClickListener {
            auth.signOut()
            val intent: Intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
            finish()

        }
        val btn_bibliotecavirtual=findViewById<ImageButton>(R.id.buscar)
        btn_bibliotecavirtual.setOnClickListener {

            val intent: Intent = Intent(this, BibliotecaVirtual:: class.java)
            startActivity(intent)


        }
        val barra=findViewById<ImageButton>(R.id.qr)
        barra.setOnClickListener {
            iniciarScanner()
        }
        val nuevoLibro=findViewById<ImageButton>(R.id.agregarLibro)
        nuevoLibro.setOnClickListener {
            val intent: Intent = Intent(this, Libros:: class.java)
            startActivity(intent)
            finish()
        }
        val calendario=findViewById<ImageButton>(R.id.reservar)
        calendario.setOnClickListener {
            val intent: Intent = Intent(this, Calendario:: class.java)
            startActivity(intent)
        }



    }

    private fun iniciarScanner() {

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Escanee el libro")
        integrator.setBeepEnabled(true)
        integrator.setTimeout(50000)
        integrator.initiateScan()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {

                db.collection("libros").document(result.contents).get().addOnSuccessListener {
                    if(it.get("titulo")== null){

                        Toast.makeText(this, "libro no encontrado", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "El libro es:"+ it.get("titulo") as String?+"de"+it.get("autor") as String?, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}