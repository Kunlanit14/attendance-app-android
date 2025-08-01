package com.example.attendanceapp.common.shareprefkeys

enum class SharePrefKeys(val data : String) {
    SAVE_DATA("saveData"),

    //Check-in and Check-out
    TIME_CHECKIN("timeCheckIn"),
    TIME_CHECKOUT("timeCheckOut"),

    //Request check-in / check-out
    DATE_REQUEST_CHECKIN("dateRequestCheckIn"),
    DATE_REQUEST_CHECKOUT("dateRequestCheckOut"),
    SAVE_TIME("timeRequest"),
    SAVE_TIME_CHECKOUT("timeReqCheckOut"),


}