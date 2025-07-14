package com.example.attendanceapp.data

data class RequestOTData(
    val currentDate : String,
    val requestType : String,
    val otDateRequest : String,
    val fromTime : String,
    val toTime : String,
    val reasonOT : String
)
