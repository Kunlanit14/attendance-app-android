package com.example.attendanceapp.model.dto.request

data class User(
    val id: Long,
    val username: String,
    val email : String,
    val firstName: String,
    val lastName : String,
    val employeeId : String,
    val department : String,
    val position : String,
    val hireDate: Long?,
    val salary : Double?,
    val role : String,
    val isActive : Boolean = true
)

data class Attendance(
    val id: Long,
    val attendanceDate: Long, // Timestamp ของเวลาเริ่มต้นของวัน
    val clockInTime: Long?,   // Timestamp ของเวลาที่ลงชื่อเข้างาน
    val clockOutTime: Long?,  // Timestamp ของเวลาที่ลงชื่อออกงาน
    val totalHours: Double?,
    val overtimeHours: Double? = 0.0,
    val status: String, // "PRESENT", "ABSENT", "LATE", "HALF_DAY"
    val notes: String?,
    val breakDurationMinutes: Int? = 0
)

data class LeaveRequestDto(
    val userId: Long,
    val leaveCategory: String,
    val leavePeriod: String,
    val startDate: Long,
    val endDate: Long,
    val reason: String
)

data class LeaveRequestResponse(
    val id: Long,
    val userId: Long,
    val leaveCategory: String,
    val leavePeriod: String,
    val startDate: Long,
    val endDate: Long,
    val reason: String,
    val status: String,
    val appliedDate: Long?,
    val approvedDate: Long?,
    val comments: String,
    val totalDays: Double?
)

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data : T
)
