package com.example.attendanceapp.view.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ButtonEnum
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys
import com.example.attendanceapp.controller.LazyHrController
import com.example.attendanceapp.model.dto.request.Attendance
import com.example.attendanceapp.model.dto.request.LeaveRequestResponse
import com.example.attendanceapp.model.dto.request.User
import com.example.attendanceapp.view.lazyHrView

class CheckInOutFragment : Fragment(), lazyHrView {

    //initial variables
    lateinit var checkInButton : Button
    lateinit var checkOutButton : Button
    lateinit var checkIn : TextView
    lateinit var checkOut : TextView
    private lateinit var controller: LazyHrController


    lateinit var sharedPreferences: SharedPreferences
    var userId : Long = 1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_check_in_out, container, false)
        checkInButton = view.findViewById(R.id.checkInButton)
        checkOutButton = view.findViewById(R.id.checkOutButton)
        checkIn = view.findViewById(R.id.tvCheckIn)
        checkOut = view.findViewById(R.id.tvCheckOut)


        controller = LazyHrController(this)
        saveCheckInListener()
        saveCheckOutListener()
        return view

    }


    override fun onResume() {
        super.onResume()
        handleButtonState()
        controller.loadTodayAttendance(userId)
    }

    fun saveCheckInListener(){

        checkOutButton.visibility = View.GONE
        checkInButton.setOnClickListener {
            val currentTime : String = getCurrentTime()
            checkIn.text = getString(R.string.check_in_at,currentTime)
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
            saveButtonState(ButtonEnum.BUTTON_STATE_OUT.state)

            controller.clockInUser(userId)
        }

    }

    fun saveCheckOutListener(){

        checkOutButton.setOnClickListener {
            val currentTimeCheckOut : String = getCurrentTime()
            checkOut.text = getString(R.string.check_out_at,currentTimeCheckOut)
            checkOutButton.visibility = View.GONE
            checkInButton.visibility = View.VISIBLE
            saveButtonState(ButtonEnum.BUTTON_STATE_IN.state)

            controller.clockOutUser(userId)
        }
    }

    fun saveButtonState(state: String){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(ButtonEnum.BUTTON_STATE.state, state)
        }

    }

    fun handleButtonState(){
        val buttonState = loadSaveButtonState()

        if(buttonState == ButtonEnum.BUTTON_STATE_IN.state){
            checkInButton.visibility = View.VISIBLE
            checkOutButton.visibility = View.GONE
        }else {
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
        }
    }

    fun loadSaveButtonState() : String {
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ButtonEnum.BUTTON_STATE.state, ButtonEnum.BUTTON_STATE_IN.state) ?: ButtonEnum.BUTTON_STATE_IN.state
    }

    fun getCurrentTime() : String {
        //Date
        val simpleTimeFormat = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        return simpleTimeFormat.format(Date().time)
    }

    override fun showLoading(isLoading: Boolean) {
        checkInButton.isEnabled != isLoading
    }

    override fun onError(message: String) {
        context?.let {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onClockInSuccess(clockInTime: Long?) {
        Toast.makeText(requireContext(), "Successfully clocked in at: $clockInTime", Toast.LENGTH_SHORT).show()
    }

    override fun onClockOutSuccess(clockOutTime: Long?) {
        Toast.makeText(requireContext(), "Successfully clocked out at: $clockOutTime", Toast.LENGTH_SHORT).show()
    }

    override fun onLeaveApplicationSuccess(leaveRequest: LeaveRequestResponse?) {}

    override fun displayUserData(user: User?) {
    }

    override fun showAttendance(attendance: Attendance?) {
        attendance?.clockInTime?.let { millis ->
            val timeText = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault()).format(
                Date(millis))
            checkIn.text = getString(R.string.check_in_at,timeText)
        }?: run {
            checkIn.text = getString(R.string.check_in_at_text)
        }

        attendance?.clockOutTime?.let { millis ->
            val timeText = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault()).format(
                Date(millis))
            checkOut.text = getString(R.string.check_out_at,timeText)
        }?: run {
            checkOut.text = getString(R.string.check_out_at_text)
        }
    }
}