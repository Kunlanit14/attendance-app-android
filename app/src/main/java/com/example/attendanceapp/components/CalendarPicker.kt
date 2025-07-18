package com.example.attendanceapp.components

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale


object CalendarPicker {
    fun showDatePicker(
        context: Context,
        dateFormat: String = "MM/dd/yyyy",
        minDate: Long? = null,
        maxDate: Long? = null,
        onDataSelected: (formattedDate : String) -> Unit
    ){
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            {_,year,month,dayOfMonth ->
                calendar.set(year,month,dayOfMonth)
                val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
                val formattedDate = formatter.format(calendar.time)

                onDataSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        minDate?.let { datePickerDialog.datePicker.minDate = it}
        maxDate?.let { datePickerDialog.datePicker.maxDate = it }

        datePickerDialog.show()
    }

}