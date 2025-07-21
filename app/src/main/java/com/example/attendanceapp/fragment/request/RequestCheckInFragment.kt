package com.example.attendanceapp.fragment.request

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ButtonEnum
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.model.RequestCheckInData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys
import java.util.Locale

open class RequestCheckInFragment : Fragment() {

    lateinit var saveButton : Button
    lateinit var btnCalendar : ImageView
    lateinit var etDateRequestcheck : TextView
    lateinit var etTimeRequestCheckIn : EditText
    lateinit var cancelButton : Button

    var requestTime: String? = null

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_request_check_in, container, false)

        saveButton = view.findViewById(R.id.btnSave)
        btnCalendar = view.findViewById(R.id.imgViewCalendar)
        etDateRequestcheck = view.findViewById(R.id.etDateCheckIn)
        etTimeRequestCheckIn = view.findViewById(R.id.etTimeCheckIn)
        cancelButton = view.findViewById(R.id.btnCancel)


        btnCalendar.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                selectedDate ->
                etDateRequestcheck.text = selectedDate

            }
        }

        saveButton.isEnabled = false

        etDateRequestcheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
            }

        })

        etTimeRequestCheckIn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { }

            override fun afterTextChanged(s: Editable?) {
                updateButtonState()

            }

        })

        saveButton.setOnClickListener {
            val timeRequest = etTimeRequestCheckIn.text.toString()

            Toast.makeText(requireContext(), getString(R.string.request_checkin_toast), Toast.LENGTH_LONG).show()

            //Date
            val dateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
            val currentDate : String = dateFormat.format(Date())

            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_CHECKIN_LIST.key,null)
            val type = object : TypeToken<MutableList<RequestCheckInData>>(){}.type
            val requestCheckInList : MutableList<RequestCheckInData> = if (json != null){
                gson.fromJson(json, type)
            }else{
                mutableListOf()
            }

            val newRequestCheckInData = RequestCheckInData(
                dateReqCheckIn = currentDate,
                requestType = RequestTypeEnum.REQUEST_CHECK_IN.type,
                requestCheckInTime = timeRequest
            )

            requestCheckInList.add(newRequestCheckInData)

            sharedPreferences.edit {
                putString(ButtonEnum.BUTTON_STATE.state, ButtonEnum.BUTTON_STATE_OUT.state)
                putString(ActivityLogKeyEnum.REQUEST_CHECKIN_LIST.key, gson.toJson(requestCheckInList))
                putString(SharePrefKeys.DATE_REQUEST_CHECKIN.data, etDateRequestcheck.text.toString())
                putString(SharePrefKeys.TIME_CHECKIN.data, timeRequest)
            }

            etDateRequestcheck.text = ""
            etTimeRequestCheckIn.text.clear()

        }

        cancelButton.setOnClickListener {
            etDateRequestcheck.text = ""
            etTimeRequestCheckIn.text.clear()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onPause() {
        super.onPause()
        saveRequestTimeCheckIn()
    }


    fun saveRequestTimeCheckIn(){

        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        requestTime = etTimeRequestCheckIn.text.toString()

        sharedPreferences.edit {
            putString(SharePrefKeys.SAVE_TIME.data, requestTime)
        }

    }

    fun updateButtonState(){

        val dateInput = etDateRequestcheck.text.toString().trim()
        val timeInput = etTimeRequestCheckIn.text.toString().trim()
        val isEnabled = dateInput.isNotEmpty() && timeInput.isNotEmpty()
        val isTimeValid = isValidTime(timeInput)

        val enableButton = isEnabled && isTimeValid

        saveButton.isEnabled = enableButton
        if(enableButton){
            saveButton.setBackgroundResource(R.drawable.button_save)
        }else{
            saveButton.setBackgroundResource(R.drawable.button_save_disable)
        }


    }

    fun isValidTime(timeString : String) : Boolean{
        val timeRegex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        return timeRegex.matches(timeString)
    }



}