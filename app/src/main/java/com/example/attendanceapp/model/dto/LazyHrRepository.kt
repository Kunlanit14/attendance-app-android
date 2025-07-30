package com.example.attendanceapp.model.dto

import com.example.attendanceapp.model.apiClient.ApiClient
import com.example.attendanceapp.model.apiService.ApiService
import com.example.attendanceapp.model.dto.request.ApiResponse
import com.example.attendanceapp.model.dto.request.User
import com.example.attendanceapp.model.dto.request.Attendance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LazyHrRepository {

    private val apiService = ApiClient.instance.create(ApiService::class.java)


    suspend fun clockIn(userId : Long) : ApiResponse<Map<String, Any>>{
        return withContext(Dispatchers.IO) {
            apiService.clockIn(userId)
        }
    }


    suspend fun clockOut(userId : Long) : ApiResponse<Map<String, Any>>{
        return withContext(Dispatchers.IO) {
            apiService.clockOut(userId)
        }
    }


    suspend fun getUserById(userId: Long) : ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            apiService.getUserById(userId)
        }
    }

    suspend fun getTodayAttendance(userId: Long) : ApiResponse<Attendance?>{
        return withContext(Dispatchers.IO) {
            apiService.getTodayAttendance(userId)
        }
    }

    suspend fun getAttendanceHistory(userId: Long): ApiResponse<List<Attendance>> {
        return withContext(Dispatchers.IO) {
            apiService.getAttendanceHistory(userId)
        }
    }
}