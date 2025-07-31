package com.example.attendanceapp.controller

import android.util.Log
import com.example.attendanceapp.model.dto.LazyHrRepository
import com.example.attendanceapp.model.dto.request.LeaveRequestDto
import com.example.attendanceapp.view.lazyHrView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class LazyHrController(private val view: lazyHrView) {

    private val repository = LazyHrRepository()
    private val controllerScope = CoroutineScope(Dispatchers.Main)

    fun clockInUser(userId: Long){
        controllerScope.launch {
            view.showLoading(true)
            try {
                val response = repository.clockIn(userId)
                print(response)
                Log.d("response", response.toString())
                withContext(Dispatchers.Main) {
                    if(response.status == "success"){
                        val data = response.data as? Map<String, Any>
                        val clockInTime = (data?.get("clockInTime") as? Number)?.toLong()
                        Log.d("clockInTime", clockInTime.toString())
                        view.onClockInSuccess(clockInTime)
                    }else{
                        view.onError("Clock-in failed: ${response.message}")
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("Network error ${e.message}")
                }
            }finally {
                withContext(Dispatchers.Main) {
                    view.showLoading(false)
                }
            }
        }
    }

    fun clockOutUser(userId: Long){
        controllerScope.launch {
            view.showLoading(true)
            try {
                val response = repository.clockOut(userId)
                print(response)
                Log.d("response", response.toString())
                withContext(Dispatchers.Main) {
                    if(response.status == "success"){
                        val data = response.data as? Map<String, Any>
                        val clockOutTime = (data?.get("clockOutTime") as? Number)?.toLong()
                        Log.d("clockOutTime", clockOutTime.toString())
                        view.onClockOutSuccess(clockOutTime)
                    }else{
                        view.onError("Clock-out failed: ${response.message}")
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("Network error ${e.message}")
                }
            }finally {
                withContext(Dispatchers.Main) {
                    view.showLoading(false)
                }
            }
        }
    }

    fun loadTodayAttendance(userId: Long){
        controllerScope.launch {
            view.showLoading(true)
            try {
                val response = repository.getTodayAttendance(userId)
                Log.d("API_RESPONSE", "getTodayAttendance: $response")
                withContext(Dispatchers.Main) {
                    if (response.status =="success" ) {
                        view.showAttendance(response.data)
                    } else {
                        view.onError(response.message)
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("Network error: ${e.message}")
                }
            }
        }
    }

    fun applyForLeave(userId: Long, reason: String){
        controllerScope.launch {
            try {
                val startTimestamp = System.currentTimeMillis()
                val endTimestamp = startTimestamp + (2 * 24 * 60 * 60 *1000)

                val leaveDto = LeaveRequestDto(
                    userId = userId,
                    leaveCategory = "ANNUAL",
                    leavePeriod = "FULL_DAY",
                    startDate = startTimestamp,
                    endDate = endTimestamp,
                    reason = reason
                )
                val response = repository.applyForLeave(leaveDto)
                withContext(Dispatchers.Main){
                    if(response.status == "success") {
                        view.onLeaveApplicationSuccess(response.data)
                    } else {
                        view.onError("Leave application failed: ${response.message}")
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    view.onError("Network error: ${e.message}")
                }
            } finally {
                withContext(Dispatchers.Main){
                    view.showLoading(false)
                }
            }
        }
    }

    fun loadUserData(userId: Long){
        controllerScope.launch {
            view.showLoading(true)
            try {
                val response = repository.getUserById(userId)
                withContext(Dispatchers.Main) {
                    if (response.status == "success"){
                        view.displayUserData(response.data)
                    }else{
                        view.onError("Failed to load user data: ${response.message}")
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("Network error: ${e.message}")
                }
            }finally {
                withContext(Dispatchers.Main){
                    view.showLoading(false)
                }
            }
        }
    }
}