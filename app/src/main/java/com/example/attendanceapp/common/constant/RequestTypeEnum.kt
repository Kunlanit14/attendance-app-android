package com.example.attendanceapp.common.constant

enum class RequestTypeEnum(val type : String) {
    CHECK_IN("Check-in"),
    CHECK_OUT("Check-out"),
    REQUEST_CHECK_IN("Request Check-in"),
    REQUEST_CHECK_OUT("Request Check-out"),
    REQUEST_OT("Request OT"),
    REQUEST_LEAVE("Request Leave"),

}