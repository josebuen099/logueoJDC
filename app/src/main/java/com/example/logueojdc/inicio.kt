package com.example.logueojdc


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.ContextThemeWrapper
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

/*Autor: Jose Esteban Bueno Sierra
Fecha:06/05/2023
Comentario:se realiza clase inicio donde se hace la conexion con la base de datos y las validaciones de los campos
*/
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
            finish()

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

                db.collection("libros").document(result.contents).get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.get("titulo") == null) {
                        val alertDialog = AlertDialog.Builder(ContextThemeWrapper(this, R.style.CustomAlertDialog))
                            .setTitle("Libro no encontrado")
                            .setMessage("El libro no fue encontrado en la base de datos.")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialogos) // Aplica el fondo personalizado
                        alertDialog.show()
                    } else {
                        // Obtener el título del libro encontrado
                        val tituloLibro = documentSnapshot.getString("titulo")
                        val autorLibro= documentSnapshot.getString("autor")
                        val descripcionLibro = documentSnapshot.getString("descripcion")
                        val generoLibro= documentSnapshot.getString("genero")


                        // Mostrar el título del libro en un cuadro de diálogo
                        val alertDialog = AlertDialog.Builder(ContextThemeWrapper(this, R.style.CustomAlertDialog))
                            .setTitle("Libro encontrado")
                            .setMessage("Título del libro: $tituloLibro \n"+"Autor del libro: $autorLibro  \n"+"Descripción del libro: $descripcionLibro \n"+"Género del libro: $generoLibro")
                            .setPositiveButton("Pedir prestado") { dialog, _ ->
                                // Acción cuando se presiona el botón "Pedir prestado"
                                // logica para pedir prestado el libro
                                val intent: Intent = Intent(this, Calendario:: class.java)
                                startActivity(intent)
                                finish()

                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancelar") { dialog, _ ->
                                // Acción cuando se presiona el botón "Cancelar"
                                dialog.dismiss()
                            }
                            .create()
                        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialogos) // Aplica el fondo personalizado
                        alertDialog.show()
                    }
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}