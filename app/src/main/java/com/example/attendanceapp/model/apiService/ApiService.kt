package com.example.attendanceapp.model.apiService

import com.example.attendanceapp.model.dto.request.ApiResponse
import com.example.attendanceapp.model.dto.request.Attendance
import com.example.attendanceapp.model.dto.request.LeaveRequestDto
import com.example.attendanceapp.model.dto.request.LeaveRequestResponse
import com.example.attendanceapp.model.dto.request.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //User
    @GET("api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): ApiResponse<User>

    //Attendance
    @POST("api/attendance/clock-in")
    suspend fun clockIn(@Query("userId") userId: Long) : ApiResponse<Map<String, Any>>

    @POST("api/attendance/clock-out")
    suspend fun clockOut(@Query("userId") userId: Long) : ApiResponse<Map<String, Any>>

    @GET("api/attendance/today/{userId}")
    suspend fun getTodayAttendance(@Path("userId") userId: Long) : ApiResponse<Attendance?>

    @GET("api/attendance/user/{userId}")
    suspend fun getUserAttendance(
        @Path("userId") userId: Long,
        @Query("startTimestamp") startTimestamp: Long,
        @Query("endTimestamp") endTimestamp : Long
    ) : ApiResponse<List<Attendance>>

    @GET("api/attendance/history/{userId}")
    suspend fun getAttendanceHistory(@Path("userId") userId: Long): ApiResponse<List<Attendance>>

    //Leave
    @POST("api/leave/apply")
    suspend fun applyForLeave(@Body leaveRequestDto: LeaveRequestDto) : ApiResponse<LeaveRequestResponse>

}