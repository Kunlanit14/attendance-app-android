package com.example.attendanceapp.screens.Request

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.attendanceapp.R
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.RequestCheckInData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date

open class RequestCheckInFragment : Fragment() {

    lateinit var saveButton : Button
    lateinit var btnCalendar : ImageView
    lateinit var etDateRequestcheck : TextView
    lateinit var etTimeRequestCheckIn : EditText
    lateinit var cancelButton : Button

    var requestTime: String? = null
    val KEY_BUTTON_STATE = "button_state"

    lateinit var sharedPreferences : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
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

        val REQUEST_CHECKIN_LIST = "request_checkin_list"


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

            Toast.makeText(requireContext(), "Request Check-in was saved.", Toast.LENGTH_LONG).show()

            //Date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy")
            val currentDate : String = dateFormat.format(Date())

            sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(REQUEST_CHECKIN_LIST,null)
            val type = object : TypeToken<MutableList<RequestCheckInData>>(){}.type
            val requestCheckInList : MutableList<RequestCheckInData> = if (json != null){
                gson.fromJson(json, type)
            }else{
                mutableListOf()
            }

            val newRequestCheckInData = RequestCheckInData(
                dateReqCheckIn = currentDate,
                requestType = "Request Check-in",
                requestCheckInTime = timeRequest
            )

            requestCheckInList.add(newRequestCheckInData)

            val editor = sharedPreferences.edit()
            editor.putString(KEY_BUTTON_STATE,"out")
            editor.putString(REQUEST_CHECKIN_LIST,gson.toJson(requestCheckInList))
            editor.putString("dateRequestCheckIn",etDateRequestcheck.text.toString())
            editor.putString("timeCheckIn",timeRequest)
            editor.apply()

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

        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        requestTime = etTimeRequestCheckIn.text.toString()

        Log.d("SharePrefTime", "Time receive: $requestTime")

        val editor = sharedPreferences.edit()

        editor.putString("timeRequest",requestTime)
        editor.apply()

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