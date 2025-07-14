package com.example.attendanceapp.data

data class RequestLeaveData(
    val currentDate : String,
    val requestType : String,
    val leaveType : String,
    val fromDate : String,
    val toDate : String,
    val period : String,
    val reasonLeave : String
)
