package com.example.attendanceapp.data.model

data class RequestOTData(
    val currentDate : String,
    val requestType : String,
    val otDateRequest : String,
    val fromTime : String,
    val toTime : String,
    val reasonOT : String
)
