package com.example.attendanceapp.common.constant

enum class RequestTypeEnum(val type : String) {
    CHECK_IN("Check-in"),
    CHECK_OUT("Check-out"),
    REQUEST_CHECK_IN("Request Check-in"),
    REQUEST_CHECK_OUT("Request Check-out"),
    REQUEST_OT("Request OT"),
    REQUEST_LEAVE("Request Leave"),

    //Spinner
    REQUEST_TYPE("requestType"),
    SPN_REQUEST_CHECKIN("Request check-in"),
    SPN_REQUEST_CHECKOUT("Request check-out"),
    SPN_REQUEST_OT("Request OT"),
    SPN_REQUEST_LEAVE("Request leave"),


    SPN_ANNUAL("Annual (P)"),
    SPN_PRIVATE_LEAVE("Private Leave (NP)"),
    SPN_SICK("Sick (P)"),
    SPN_SPECIAL_HOLIDAY("Special Holiday (P)"),
    SPN_PERSONAL_LEAVE("Personal Leave (P)"),

    SPN_AM("AM"),
    SPN_PM("PM"),
    SPN_FULL_DAY("Full Day"),

    ANNUAL("ANNUAL"),
    PRIVATE_LEAVE("PRIVATE"),
    SICK("SICK"),
    SPECIAL_HOLIDAY("SPECIAL_HOLIDAY"),
    PERSONAL_LEAVE("PERSONAL_LEAVE"),

    AM("AM"),
    PM("PM"),
    FULL_DAY("FULL_DAY"),


}