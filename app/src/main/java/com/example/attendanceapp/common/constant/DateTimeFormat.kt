package com.example.attendanceapp.common.constant

enum class DateTimeFormat(val format : String) {
    DATE_PATTERN("MMM dd, yyyy"),
    TIME_PATTERN("HH:mm"),

    CALENDAR_INPUT("dd MMMM, yyyy")
}