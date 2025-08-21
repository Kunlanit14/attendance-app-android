package com.example.attendanceapp.view.request

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ButtonEnum
import com.example.attendanceapp.model.dto.data.RequestCheckInData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys
import com.example.attendanceapp.components.CustomCalendar
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

        handleCalendar()
        saveDataRequestCheckIn()
        clearDataRequestCheckIn()
        handleDateTextChanged()
        handleTimeTextChanged()

        return view
    }

    override fun onPause() {
        super.onPause()
        saveRequestTimeCheckIn()
    }

    private val calendarLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == android.app.Activity.RESULT_OK) {
            val date = result.data?.getStringExtra("selectDate")
            etDateRequestcheck.text = date ?: ""
        }
    }


    fun saveRequestTimeCheckIn(){

        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        requestTime = etTimeRequestCheckIn.text.toString()
        sharedPreferences.edit {
            putString(SharePrefKeys.SAVE_TIME.data, requestTime)
        }

    }

    fun updateButtonStateRequestCheckIn(){

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

    fun handleCalendar(){
        btnCalendar.setOnClickListener {
            val intent = Intent(requireActivity(), CustomCalendar::class.java)
            calendarLauncher.launch(intent)

        }
    }

    fun saveDataRequestCheckIn(){
        saveButton.isEnabled = false
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
            clearDataSaved()

        }
    }

    fun clearDataRequestCheckIn(){
        cancelButton.setOnClickListener {
            clearDataSaved()
        }
    }

    fun clearDataSaved(){
        etDateRequestcheck.text = ""
        etTimeRequestCheckIn.text.clear()
    }

    fun handleDateTextChanged(){
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
                updateButtonStateRequestCheckIn()
            }

        })
    }

    fun handleTimeTextChanged(){
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
                updateButtonStateRequestCheckIn()

            }

        })
    }

}