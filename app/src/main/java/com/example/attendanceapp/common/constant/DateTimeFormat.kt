package com.example.attendanceapp.common.constant

enum class DateTimeFormat(val format : String) {
    DATE_PATTERN("MMM dd, yyyy"),
    PARSE_DATE_PATTERN("MM/dd/yyyy"),
    TIME_PATTERN("HH:mm")
}