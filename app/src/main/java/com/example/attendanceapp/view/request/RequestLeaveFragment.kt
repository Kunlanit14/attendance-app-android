package com.example.attendanceapp.view.request

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.example.attendanceapp.R
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.model.dto.data.RequestLeaveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys
import com.example.attendanceapp.controller.LazyHrController
import com.example.attendanceapp.model.dto.request.Attendance
import com.example.attendanceapp.model.dto.request.LeaveRequestDto
import com.example.attendanceapp.model.dto.request.LeaveRequestResponse
import com.example.attendanceapp.model.dto.request.User
import com.example.attendanceapp.view.lazyHrView
import java.util.Calendar

class RequestLeaveFragment : Fragment(), lazyHrView {

    lateinit var btnCalendarFromDate : ImageView
    lateinit var btnCalendarToDate : ImageView
    lateinit var etFromDate : TextView
    lateinit var etToDate : TextView
    lateinit var leaveTypeSpinner : Spinner
    lateinit var periodSpinner : Spinner
    lateinit var btnLeaveSave : Button
    lateinit var btnLeaveCancel : Button
    lateinit var etLeaveReason : EditText

    private lateinit var controller: LazyHrController
    var userId : Long = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request_leave, container, false)

        btnCalendarFromDate = view.findViewById(R.id.imgCalendarFromDate)
        btnCalendarToDate = view.findViewById(R.id.imgCalendarToDate)
        etFromDate = view.findViewById(R.id.etFromDateLeave)
        etToDate = view.findViewById(R.id.etToDateLeave)
        leaveTypeSpinner = view.findViewById(R.id.spinnerLeaveType)
        periodSpinner = view.findViewById(R.id.spinnerPeriod)
        btnLeaveSave = view.findViewById(R.id.btnSaveLeave)
        btnLeaveCancel = view.findViewById(R.id.btnCancelLeave)
        etLeaveReason = view.findViewById(R.id.etReasonLeave)


        handleCalendarFromDate()
        handleCalendarToDate()
        spinnerLeaveTypeAdapter()
        spinnerPeriodAdapter()
        saveDataRequestLeave()
        clearDataRequestLeave()
        handleFromDateTextChanged()
        handleToDateTextChanged()
        handleReasonTextChanged()
        controller = LazyHrController(this)


        // Inflate the layout for this fragment
        return view
    }

    fun updateButtonStateLeave(){
        val selectedLeaveType = leaveTypeSpinner.isNotEmpty()
        val selectedPeriodType = periodSpinner.isNotEmpty()
        val fromDateLeave = etFromDate.text.toString()
        val toDateLeave = etToDate.text.toString()
        val reasonLeave = etLeaveReason.text.toString()

        val enabledSaveLeave = selectedLeaveType && selectedPeriodType && fromDateLeave.isNotEmpty() && toDateLeave.isNotEmpty() && reasonLeave.isNotEmpty()

        btnLeaveSave.isEnabled = enabledSaveLeave
        if(enabledSaveLeave){
            btnLeaveSave.setBackgroundResource(R.drawable.button_save)
        }else{
            btnLeaveSave.setBackgroundResource(R.drawable.button_save_disable)
        }
    }

    fun handleCalendarFromDate(){
        btnCalendarFromDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etFromDate.text = selectedDate
            }
        }
    }

    fun handleCalendarToDate(){
        btnCalendarToDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etToDate.text = selectedDate
            }
        }
    }

    fun spinnerLeaveTypeAdapter(){
        //Spinner Leave Type
        val leaveTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.LeaveType,
            android.R.layout.simple_spinner_dropdown_item
        )

        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leaveTypeSpinner.adapter = leaveTypeAdapter
        spinnerLeaveTypeItemSelected()
    }

    fun spinnerLeaveTypeItemSelected() {
        //Handle leave selection
        leaveTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateButtonStateLeave()
                val selectItem = parent?.getItemAtPosition(position).toString()
                if (position > 0){
                    Toast.makeText(requireContext(), selectItem, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun spinnerPeriodAdapter(){
        //Spinner Period Type
        val periodAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Period,
            android.R.layout.simple_spinner_dropdown_item
        )

        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        periodSpinner.adapter = periodAdapter
        spinnerPeriodItemSelected()
    }

    fun spinnerPeriodItemSelected() {
        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateButtonStateLeave()
                val selectItem = parent?.getItemAtPosition(position).toString()
                if (position > 0){
                    Toast.makeText(requireContext(), selectItem, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    fun saveDataRequestLeave(){
        btnLeaveSave.isEnabled = false
        btnLeaveSave.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.request_leave_toast), Toast.LENGTH_LONG).show()

            val inputFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.ENGLISH)
            val startDate = inputFormat.parse(etFromDate.text.toString())?.time ?: 0
            val endDate = inputFormat.parse(etToDate.text.toString())?.time ?: 0

            if (startDate < System.currentTimeMillis()) {
                return@setOnClickListener
            }
            val leaveCategoryMap = mapOf(
                RequestTypeEnum.SPN_ANNUAL.type to RequestTypeEnum.ANNUAL.type,
                RequestTypeEnum.SPN_PRIVATE_LEAVE to RequestTypeEnum.PRIVATE_LEAVE.type,
                RequestTypeEnum.SPN_SICK.type to RequestTypeEnum.SICK.type,
                RequestTypeEnum.SPN_SPECIAL_HOLIDAY.type to RequestTypeEnum.SPECIAL_HOLIDAY.type,
                RequestTypeEnum.SPN_PERSONAL_LEAVE.type to RequestTypeEnum.PERSONAL_LEAVE.type,

            )

            val periodMap = mapOf(
                RequestTypeEnum.SPN_AM.type to RequestTypeEnum.AM.type,
                RequestTypeEnum.SPN_PM.type to RequestTypeEnum.PM.type,
                RequestTypeEnum.SPN_FULL_DAY.type to RequestTypeEnum.FULL_DAY.type,
            )

            val selectedLeaveCategory = leaveCategoryMap[leaveTypeSpinner.selectedItem.toString()] ?: ""
            val selectedLeavePeriod = periodMap[periodSpinner.selectedItem.toString()] ?: ""

            val newRequestLeaveData = LeaveRequestDto(
                userId = userId,
                leaveCategory = selectedLeaveCategory,
                leavePeriod = selectedLeavePeriod,
                startDate = startDate,
                endDate = endDate,
                reason = etLeaveReason.text.toString()
            )

            controller.applyForLeave(newRequestLeaveData)
            clearDataSaved()

        }
    }

    fun clearDataRequestLeave(){
        btnLeaveCancel.setOnClickListener {
            clearDataSaved()
        }
    }

    fun clearDataSaved(){
        leaveTypeSpinner.setSelection(0)
        periodSpinner.setSelection(0)
        etFromDate.text = ""
        etToDate.text = ""
        etLeaveReason.text.clear()
    }

    fun handleFromDateTextChanged(){
        etFromDate.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {
                updateButtonStateLeave()
            }

        })
    }

    fun handleToDateTextChanged(){
        etToDate.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {
                updateButtonStateLeave()
            }

        })
    }

    fun handleReasonTextChanged(){
        etLeaveReason.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {
                updateButtonStateLeave()
            }

        })
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun onError(message: String) {
    }

    override fun onClockInSuccess(clockInTime: Long?) {
    }

    override fun onClockOutSuccess(clockOutTime: Long?) {
    }

    override fun onLeaveApplicationSuccess(leaveRequest: LeaveRequestResponse?) {
        Toast.makeText(requireContext(), "Leave submitted successfully with ID: ${leaveRequest?.id}", Toast.LENGTH_SHORT).show()
    }


    override fun displayUserData(user: User?) {
    }

    override fun showAttendance(attendance: Attendance?) {
    }


}