package com.example.logueojdc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList


class Calendario : AppCompatActivity() {
    private lateinit var monthYearTextView: TextView
    private lateinit var calendarGridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        monthYearTextView = findViewById(R.id.monthYearTextView)
        calendarGridView = findViewById(R.id.calendarGridView)

        // Obtener la fecha actual
        val currentDate = Calendar.getInstance()

        // Obtener el mes y año actual
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentYear = currentDate.get(Calendar.YEAR)

        // Mostrar el mes y año actual en el TextView
        val monthYearText = DateFormatSymbols.getInstance().months[currentMonth] + " " + currentYear
        monthYearTextView.text = monthYearText

        // Obtener los días del mes actual en un ArrayList
        val daysOfMonth = getDaysOfMonth(currentMonth, currentYear)

        // Crear un adaptador para el GridView con los días del mes
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, daysOfMonth)

        // Establecer el adaptador en el GridView
        calendarGridView.adapter = adapter

        // Manejar el evento de clic en un día del calendario
        calendarGridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedDate = daysOfMonth[position]
                // Realizar acciones según el día seleccionado
            }
    }

    private fun getDaysOfMonth(month: Int, year: Int): ArrayList<String> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val daysOfMonth = ArrayList<String>()

        // Obtener el número de días en el mes actual
        val numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Obtener el día de la semana en el que comienza el mes
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Agregar días vacíos al ArrayList para alinear el calendario
        for (i in 1 until firstDayOfWeek) {
            daysOfMonth.add("")
        }

        // Agregar los días del mes al ArrayList
        for (i in 1..numDays) {
            daysOfMonth.add(i.toString())
        }

        return daysOfMonth
    }
}