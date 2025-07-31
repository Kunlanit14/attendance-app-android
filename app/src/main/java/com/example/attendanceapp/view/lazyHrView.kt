package com.example.attendanceapp.view

import com.example.attendanceapp.model.dto.request.Attendance
import com.example.attendanceapp.model.dto.request.User

interface lazyHrView {
    fun showLoading(isLoading: Boolean)
    fun onError(message: String)
    fun onClockInSuccess(clockInTime: Long?)
    fun onClockOutSuccess(clockOutTime: Long?)
//    fun onLeaveApplicationSuccess(leaveRequest: LeaveRequest?)
    fun displayUserData(user: User?)
    fun showAttendance(attendance: Attendance?)
}