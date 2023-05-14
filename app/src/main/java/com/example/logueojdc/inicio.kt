package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

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



    }

    private fun iniciarScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Escanee el libro")

        integrator.setTorchEnabled(false)
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