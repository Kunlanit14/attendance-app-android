package com.example.attendanceapp.model.dto.data

data class MonthData(
    val monthNumber: Int,
    val monthName : String,
    val days: List<DayData>
)

data class DayData(
    val dayNumber: Int,
    val isToday: Boolean = false,
    val isHoliday: Boolean = false
)
