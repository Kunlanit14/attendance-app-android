package com.example.attendanceapp.controller

import android.util.Log
import com.example.attendanceapp.model.dto.LazyHrRepository
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
                        view.onError("การลงเวลาเข้างานล้มเหลว: ${response.message}")
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("ข้อผิดพลาดเครือข่าย ${e.message}")
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
                withContext(Dispatchers.Main) {
                    if (response.status =="success" ) {
                        view.showAttendance(response.data)
                    } else {
                        view.onError(response.message)
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("เกิดข้อผิดพลาด: ${e.message}")
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
                        view.onError("โหลดข้อมูลผู้ใช้ล้มเหลว: ${response.message}")
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    view.onError("ข้อผิดพลาดเครือข่าย: ${e.message}")
                }
            }finally {
                withContext(Dispatchers.Main){
                    view.showLoading(false)
                }
            }
        }
    }
}