package com.example.attendanceapp.fragment.request

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.model.RequestOTData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestOTFragment : Fragment() {

    lateinit var btnCalendarOT : ImageView
    lateinit var etDateRequestOT : TextView
    lateinit var btnSaveOT : TextView
    lateinit var btnCancelOT : Button
    lateinit var etFromTimeReqOT : EditText
    lateinit var etToTimeReqOT : EditText
    lateinit var etReason : EditText
    lateinit var checkBoxBreak : CheckBox


    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request_o_t, container, false)

        btnCalendarOT = view.findViewById(R.id.imgViewCalendarOT)
        etDateRequestOT = view.findViewById(R.id.etDateRequestOT)
        btnSaveOT = view.findViewById(R.id.btnSaveOT)
        btnCancelOT = view.findViewById(R.id.btnCancelOT)
        etFromTimeReqOT = view.findViewById(R.id.etFromTimeRequestOT)
        etToTimeReqOT = view.findViewById(R.id.etTimeRequestOT)
        etReason = view.findViewById(R.id.etReason)
        checkBoxBreak = view.findViewById(R.id.checkBox)

        handleCalendar()
        saveDataRequestOT()
        clearDataRequestOT()
        handleCheckBoxBreak()
        handleDateTextChanged()
        handleFromTimeTextChanged()
        handleToTimeTextChanged()
        handleReasonTextChanged()

        return view
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        val checkInTime = sharedPreferences.getString(SharePrefKeys.TIME_CHECKIN.data,"")
        val formatDate = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        if (!checkInTime.isNullOrEmpty()){
            try {
                val date = formatDate.parse(checkInTime)
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.HOUR, 9)
                val fromTimeOff = formatDate.format(calendar.time)
                etFromTimeReqOT.setText(fromTimeOff)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val checkOutTime = sharedPreferences.getString(SharePrefKeys.TIME_CHECKOUT.data,"")
        if (!checkInTime.isNullOrEmpty()){
            etToTimeReqOT.setText(checkOutTime)
        }
    }


    fun updateButtonStateReqOT(){
        val dateReqOT = etDateRequestOT.text.toString().trim()
        val fromTimeOT = etFromTimeReqOT.text.toString().trim()
        val toTimeOT = etToTimeReqOT.text.toString().trim()
        val reason = etReason.text.toString().trim()
        val isEnable = dateReqOT.isNotEmpty() && fromTimeOT.isNotEmpty() && toTimeOT.isNotEmpty() && reason.isNotEmpty()
        val isFromTimeValid = isValidTime(fromTimeOT)
        val isToTimeValid = isValidTime(toTimeOT)

        val enableButton = isEnable && isFromTimeValid && isToTimeValid

        btnSaveOT.isEnabled = enableButton
        if(enableButton){
            btnSaveOT.setBackgroundResource(R.drawable.button_save)
        }else{
            btnSaveOT.setBackgroundResource(R.drawable.button_save_disable)
        }
    }

    fun isValidTime(timeString : String) : Boolean{
        val timeRegex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        return timeRegex.matches(timeString)
    }

    fun checkedBreakFromTimeProcess(){
        val fromTimeInput = etFromTimeReqOT.text.toString()
        if (fromTimeInput.isEmpty())return
        val formatDate = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        val date = formatDate.parse(fromTimeInput)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, 20)
        val fromTimeWithBreak = formatDate.format(calendar.time)
        etFromTimeReqOT.setText(fromTimeWithBreak)
    }

    fun uncheckedBreakFromTimeProcess(){
        val fromTimeInput = etFromTimeReqOT.text.toString()
        if (fromTimeInput.isEmpty())return
        val formatDate = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        val date = formatDate.parse(fromTimeInput)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, -20)
        val fromTimeWithBreak = formatDate.format(calendar.time)
        etFromTimeReqOT.setText(fromTimeWithBreak)
    }

    fun checkedBreakToTimeProcess(){
        val toTimeInput = etToTimeReqOT.text.toString()
        if (toTimeInput.isEmpty())return
        val formatDate = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        val date = formatDate.parse(toTimeInput)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, 20)
        val toTimeWithBreak = formatDate.format(calendar.time)
        etToTimeReqOT.setText(toTimeWithBreak)
    }

    fun uncheckedBreakToTimeProcess(){
        val toTimeInput = etToTimeReqOT.text.toString()
        if (toTimeInput.isEmpty())return
        val formatDate = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        val date = formatDate.parse(toTimeInput)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, -20)
        val toTimeWithBreak = formatDate.format(calendar.time)
        etToTimeReqOT.setText(toTimeWithBreak)
    }

    fun handleCalendar(){
        btnCalendarOT.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etDateRequestOT.text = selectedDate
            }
        }
    }

    fun saveDataRequestOT(){
        btnSaveOT.isEnabled = false
        btnSaveOT.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.request_ot_toast), Toast.LENGTH_LONG).show()

            //Date
            val dateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
            val currentDate : String = dateFormat.format(Date())

            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_OT_LIST.key,null)
            val type = object : TypeToken<MutableList<RequestOTData>>(){}.type
            val requestOTDataList : MutableList<RequestOTData> = if (json != null) {
                gson.fromJson(json, type)
            } else {
                mutableListOf()
            }

            val newRequestOTData = RequestOTData(
                currentDate = currentDate,
                requestType = RequestTypeEnum.REQUEST_OT.type,
                otDateRequest = currentDate,
                fromTime = etFromTimeReqOT.text.toString(),
                toTime = etToTimeReqOT.text.toString(),
                reasonOT = etReason.text.toString(),
            )
            requestOTDataList.add(newRequestOTData)

            sharedPreferences.edit {
                putString(ActivityLogKeyEnum.REQUEST_OT_LIST.key,gson.toJson(requestOTDataList))
            }

            clearDataSaved()
        }
    }
    fun clearDataRequestOT(){
        btnCancelOT.setOnClickListener {
            clearDataSaved()
        }
    }

    fun clearDataSaved() {
        etDateRequestOT.text = ""
        etFromTimeReqOT.text.clear()
        etToTimeReqOT.text.clear()
        etReason.text.clear()
        checkBoxBreak.isChecked = false
    }

    fun handleCheckBoxBreak(){
        checkBoxBreak.setOnCheckedChangeListener { checkBox, isChecked ->
            if(isChecked) {
                checkedBreakFromTimeProcess()
                checkedBreakToTimeProcess()
            } else {
                uncheckedBreakFromTimeProcess()
                uncheckedBreakToTimeProcess()
            }
        }
    }

    fun handleDateTextChanged(){
        etDateRequestOT.addTextChangedListener(object : TextWatcher{
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
                updateButtonStateReqOT()
            }

        })
    }

    fun handleFromTimeTextChanged(){
        etFromTimeReqOT.addTextChangedListener(object : TextWatcher{
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
                updateButtonStateReqOT()
            }

        })
    }

    fun handleToTimeTextChanged(){
        etToTimeReqOT.addTextChangedListener(object : TextWatcher{
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
                updateButtonStateReqOT()
            }

        })
    }

    fun handleReasonTextChanged(){
        etReason.addTextChangedListener(object : TextWatcher{
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
                updateButtonStateReqOT()
            }

        })
    }

}