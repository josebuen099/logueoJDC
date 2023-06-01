package com.example.logueojdc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
/*Autor: Jose Esteban Bueno Sierra
Fecha:11/05/2023
Comentario:se realiza layout y clase en la cual se agregan libros a la base de datos firestone donde se imprementa el scanneo de codigo del libro
y los campos necesarios para su identificacion a su ves se realizan las validaciones de los campos y la opcion de que si el libro ya existe lo carga directamente
cosa que en la version inicial se hacia con un boton, se implementa lista desplegable que permite seleccionar el genero del libro.
*/
class Libros : AppCompatActivity() {
    private val db= FirebaseFirestore.getInstance()

    fun load(){
        val id1 = findViewById<TextView>(R.id.idLibro)
        val id = id1.text.toString()
        val titulo1 = findViewById<TextView>(R.id.titulo)
        val autor1 = findViewById<TextView>(R.id.autor)
        val genero1 = findViewById<Spinner>(R.id.spinner)
        val descripcion1 = findViewById<TextView>(R.id.descripcion)
        val tiempoPrestamo1=findViewById<TextView>(R.id.tiempoPrestamo)
        if (id == "") {
            Toast.makeText(this, "El Id esta vacio,por favor escanee el código ", Toast.LENGTH_SHORT).show()

        }else{
            db.collection("libros").document(id).get().addOnSuccessListener { documentSnapshot ->
                titulo1.setText(documentSnapshot.get("titulo") as String?)
                autor1.setText(documentSnapshot.get("autor") as String?)
                descripcion1.setText(documentSnapshot.get("descripcion") as String?)
                tiempoPrestamo1.setText(documentSnapshot.get("tiempo_prestamo") as String?)

                val genero = documentSnapshot.get("genero") as String?

                // Determinar el índice correspondiente al valor del género en el adaptador del Spinner
                val adapter = genero1.adapter
                val count = adapter.count

                for (i in 0 until count) {
                    val item = adapter.getItem(i) as String
                    if (item == genero) {
                        genero1.setSelection(i)
                        break
                    }
                }
            }

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libros)
        val spinner: Spinner = findViewById<Spinner>(R.id.spinner)
        val elementos = arrayOf("Genero", "Accion", " Ciencia","Drama", "Comedia","Deporte", "Fisica")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, elementos)
        spinner.adapter = adapter
       

fun limpiar(){

        val id1 = findViewById<TextView>(R.id.idLibro)
        val titulo1 = findViewById<TextView>(R.id.titulo)
        val autor1 = findViewById<TextView>(R.id.autor)
        val descripcion1 = findViewById<TextView>(R.id.descripcion)
        val tiempoPrestamo1=findViewById<TextView>(R.id.tiempoPrestamo)
        id1.setText("")
        titulo1.setText("")
        autor1.setText("")
        spinner.setSelection(0)
        descripcion1.setText("")
        tiempoPrestamo1.setText("")


}
        val btn_borrarcontenido=findViewById<ImageButton>(R.id.clear)
        btn_borrarcontenido.setOnClickListener {
           limpiar()
        }

        val btn_guarda= findViewById<ImageButton>(R.id.savelibro)
        btn_guarda.setOnClickListener {
            val id1 = findViewById<TextView>(R.id.idLibro)
            val id = id1.text.toString()
            val titulo1 = findViewById<TextView>(R.id.titulo)
            val titulo = titulo1.text.toString()
            val autor1 = findViewById<TextView>(R.id.autor)
            val autor = autor1.text.toString()
            val genero = spinner.selectedItem.toString()

            val descripcion1 = findViewById<TextView>(R.id.descripcion)
            val descripcion = descripcion1.text.toString()
            val tiempoPrestamo1=findViewById<TextView>(R.id.tiempoPrestamo)
            val tiempoPrestamo = tiempoPrestamo1.text.toString()

            if(id.isEmpty()|| titulo.isEmpty()|| autor.isEmpty() || genero.equals("Genero")||descripcion.isEmpty() ){
                Toast.makeText(this, "Complete todos los campos ", Toast.LENGTH_SHORT).show()

            }else{
            Toast.makeText(this, "Libro guardado con exito... ", Toast.LENGTH_SHORT).show()
            db.collection("libros").document(id).set(
                hashMapOf("titulo" to titulo,
                "autor" to autor,
                "genero" to genero,
                "descripcion" to descripcion,
                "tiempo_prestamo" to tiempoPrestamo)

            )
                limpiar()
            }


        }
        val home1 =findViewById<Button>(R.id.regresarinicio)
        home1.setOnClickListener {
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)
            finish()

        }
        val btn_scan=findViewById<ImageButton>(R.id.scanqr)
        btn_scan.setOnClickListener {
iniciarScanner()
        }
        val btn_delete=findViewById<ImageButton>(R.id.deletelibro)
        btn_delete.setOnClickListener {
            val id1 = findViewById<TextView>(R.id.idLibro)
            val id = id1.text.toString()
            if(id.isEmpty()){
                Toast.makeText(this, "Id vacio", Toast.LENGTH_SHORT).show()
            }else {

                db.collection("libros").document(id).delete()
                Toast.makeText(this, "Se ha elimininado el libro", Toast.LENGTH_SHORT).show()
                limpiar()
            } }
    }
    private fun iniciarScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Escanee el libro")

        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.setTimeout(50000)
        integrator.initiateScan()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val id1 = findViewById<TextView>(R.id.idLibro)
               id1.text=result.contents
                load()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}