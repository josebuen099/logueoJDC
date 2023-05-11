package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator

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
        val barra=findViewById<ImageButton>(R.id.qr)
        barra.setOnClickListener {
            iniciarScanner()
        }



    }

    private fun bdlibros() {
        val database = FirebaseDatabase.getInstance()
        val bookRef = database.getReference("libros")
        val bookId = bookRef.push().key

      //  val book = Book(bookId, "El Quijote", "Miguel de Cervantes", "Novela", "La historia de un hidalgo que pierde el juicio después de leer demasiados libros de caballería.")
    }

    private fun iniciarScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Escanne el libro")
        integrator.setPrompt("JDC")
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
                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}