package com.example.logueojdc

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
/*Autor: Jose Esteban Bueno Sierra
Fecha:23/05/2023
Comentario:se realiza clase calendario para guardar los eventos en el celular con el fin de que se le notifique al usuario del tiempo.
*/
class Calendario : AppCompatActivity() {

    private lateinit var monthYearTextView: TextView
    private lateinit var calendarGridView: GridView
    private val PERMISSIONS_REQUEST_CALENDAR = 1

    private fun checkCalendarPermissions() {
        val permisos = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )

        val permisosARequerir = mutableListOf<String>()
        for (permiso in permisos) {
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                permisosARequerir.add(permiso)
            }
        }

        if (permisosARequerir.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permisosARequerir.toTypedArray(),
                PERMISSIONS_REQUEST_CALENDAR
            )
        } else {
            // Permisos ya otorgados, puedes acceder al calendario aquí
            obtenerEventosCalendario()
            agregarRecordatorioAlCalendario()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CALENDAR) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permisos otorgados, puedes acceder al calendario aquí
                obtenerEventosCalendario()
                agregarRecordatorioAlCalendario()
            } else {
                Toast.makeText(this, "No tiene permiso", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        checkCalendarPermissions()
        monthYearTextView = findViewById(R.id.monthYearTextView)
        calendarGridView = findViewById(R.id.calendarGridView)

        // Obtener la fecha actual
        val fechaActual = Calendar.getInstance()

        // Obtener el mes y año actual
        val mesActual = fechaActual.get(Calendar.MONTH)
        val añoActual = fechaActual.get(Calendar.YEAR)

        // Mostrar el mes y año actual en el TextView
        val textoMesAño = DateFormatSymbols.getInstance().months[mesActual] + " " + añoActual
        monthYearTextView.text = textoMesAño

        // Obtener los días del mes actual en un ArrayList
        val diasDelMes = obtenerDiasDelMes(mesActual, añoActual)

        // Crear un adaptador para el GridView con los días del mes
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, diasDelMes)

        // Establecer el adaptador en el GridView
        calendarGridView.adapter = adaptador

        // Manejar el evento de clic en un día del calendario
        calendarGridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val fechaSeleccionada = diasDelMes[position]
                // Cambiar el fondo del elemento view para indicar que tiene un evento
                view.setBackgroundColor(Color.YELLOW)
            }
    }

    private fun tieneEvento(fecha: String): Boolean {
        return fecha.isNotEmpty()
    }

    @SuppressLint("Range")
    private fun obtenerEventosCalendario() {
        val uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        val selection = "${CalendarContract.Events.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf("1") // ID del calendario, puede variar según el dispositivo

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        cursor?.use {
            while (it.moveToNext()) {
                val titulo = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                val descripcion = it.getString(it.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                val inicioMillis = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                val finMillis = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))
                val textoEventos = findViewById<TextView>(R.id.dinamico)

                //datos de los eventos obtenidos
                textoEventos.setText("$titulo, $descripcion")
            }
        }
    }

    private fun agregarRecordatorioAlCalendario() {
        val idCalendario: Long = 1 // ID del calendario
        val inicioMillis: Long = System.currentTimeMillis() + (3 * 60 * 1000)
        val finMillis: Long = inicioMillis + 60 * 60 * 1000 // Duración del evento en milisegundos

        val cr: ContentResolver = contentResolver
        val valores = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, idCalendario)
            put(CalendarContract.Events.TITLE, "Entrega del libro")
            put(CalendarContract.Events.DESCRIPTION, "Recuerda devolver el libro para evitar sanciones")
            put(CalendarContract.Events.EVENT_LOCATION, "Fundación Universitaria Juan de Castellanos")
            put(CalendarContract.Events.DTSTART, inicioMillis)
            put(CalendarContract.Events.DTEND, finMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, "UTC")
            put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        }

        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, valores)
        val idEvento = uri?.lastPathSegment?.toLongOrNull()

        if (idEvento != null) {
            Toast.makeText(this, "Evento guardado con ID: $idEvento", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al guardar el evento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerDiasDelMes(mes: Int, año: Int): ArrayList<String> {
        val calendario = Calendar.getInstance()
        calendario.set(año, mes, 1)
        val diasDelMes = ArrayList<String>()

        // Obtener el número de días en el mes actual
        val numDias = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Obtener el día de la semana en el que comienza el mes
        val primerDiaSemana = calendario.get(Calendar.DAY_OF_WEEK)

        // Agregar días vacíos al ArrayList para alinear el calendario
        for (i in 1 until primerDiaSemana) {
            diasDelMes.add("")
        }

        // Agregar los días del mes al ArrayList
        for (i in 1..numDias) {
            diasDelMes.add(i.toString())
        }

        return diasDelMes
    }
}

