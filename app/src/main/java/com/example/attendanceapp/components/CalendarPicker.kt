package com.example.attendanceapp.components

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Locale

object CalendarPicker {
    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePicker(context: Context,onDateSelected: (formattedDate: String) -> Unit){
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,{DatePicker, year:Int, monthOfYear:Int, dayOfMonth:Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year,monthOfYear,dayOfMonth)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}