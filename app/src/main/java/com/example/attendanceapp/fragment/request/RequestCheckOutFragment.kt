package com.example.attendanceapp.fragment.request

import android.content.Context
import android.content.SharedPreferences
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
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.ButtonEnum
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.model.RequestCheckOutData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys

class RequestCheckOutFragment : Fragment() {

    lateinit var btnCalendar : ImageView
    lateinit var saveButtonReqCheckOut : Button
    lateinit var etDateReqCheckOut : TextView
    lateinit var etTimeReqCheckout : EditText
    lateinit var cancelButtonReqCheckOut : Button

    var requestTimeCheckOut : String? = null

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request_check_out, container, false)
        // Inflate the layout for this fragment


        btnCalendar = view.findViewById(R.id.imageViewCalendarCheckOut)
        etDateReqCheckOut = view.findViewById(R.id.etDateCheckOut)
        etTimeReqCheckout = view.findViewById(R.id.etTimeCheckOut)
        saveButtonReqCheckOut = view.findViewById(R.id.btnSaveCheckOut)
        cancelButtonReqCheckOut = view.findViewById(R.id.btnCancelCheckOut)


        btnCalendar.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etDateReqCheckOut.text = selectedDate
            }
        }

        etDateReqCheckOut.addTextChangedListener(object  : TextWatcher{
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

            override fun afterTextChanged(s: Editable?)
            {
                updateButtonStateReqCheckOut()
            }

        })

        etTimeReqCheckout.addTextChangedListener(object : TextWatcher{
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
            ) {

            }

            override fun afterTextChanged(s: Editable?)
            {
                updateButtonStateReqCheckOut()
            }

        })

        saveButtonReqCheckOut.isEnabled = false
        saveButtonReqCheckOut.setOnClickListener {
            val timeReqCheckOut = etTimeReqCheckout.text.toString()

            Toast.makeText(requireContext(), getString(R.string.request_checkout_toast), Toast.LENGTH_LONG).show()
            //Date
            val dateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
            val currentDate : String = dateFormat.format(Date())

            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

            val gson = Gson()
            val json = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_CHECKOUT_LIST.key,null)
            val type = object : TypeToken<MutableList<RequestCheckOutData>>(){}.type
            val requestCheckOutList : MutableList<RequestCheckOutData> = if (json != null){
                gson.fromJson(json, type)
            }else{
                mutableListOf()
            }

            val newRequestCheckOutData = RequestCheckOutData(
                dateReqCheckOut = currentDate,
                requestType = RequestTypeEnum.REQUEST_CHECK_OUT.type,
                requestCheckOutTime = timeReqCheckOut
            )

            requestCheckOutList.add(newRequestCheckOutData)

            sharedPreferences.edit {
                putString(ButtonEnum.BUTTON_STATE.state, ButtonEnum.BUTTON_STATE_IN.state)
                putString(
                    ActivityLogKeyEnum.REQUEST_CHECKOUT_LIST.key,
                    gson.toJson(requestCheckOutList)
                )
                putString(SharePrefKeys.DATE_REQUEST_CHECKOUT.data, etDateReqCheckOut.text.toString())
                putString(SharePrefKeys.TIME_CHECKOUT.data, timeReqCheckOut)
            }

            etDateReqCheckOut.text = ""
            etTimeReqCheckout.text.clear()

        }

        cancelButtonReqCheckOut.setOnClickListener {
            etDateReqCheckOut.text = ""
            etTimeReqCheckout.text.clear()
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        saveRequestTimeCheckOut()
    }

    fun updateButtonStateReqCheckOut(){
        val dateReqCheckOutInput = etDateReqCheckOut.text.toString().trim()
        val timeReqCheckOutInput = etTimeReqCheckout.text.toString().trim()
        val isEnable = dateReqCheckOutInput.isNotEmpty() && timeReqCheckOutInput.isNotEmpty()
        val isTimeValid = isValidTime(timeReqCheckOutInput)

        val enableButton = isEnable && isTimeValid

        saveButtonReqCheckOut.isEnabled = enableButton
        if(enableButton){
            saveButtonReqCheckOut.setBackgroundResource(R.drawable.button_save)
        }else{
            saveButtonReqCheckOut.setBackgroundResource(R.drawable.button_save_disable)
        }


    }

    fun isValidTime(timeString : String) : Boolean{
        val timeRegex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        return timeRegex.matches(timeString)
    }

    fun saveRequestTimeCheckOut(){

        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        requestTimeCheckOut = etTimeReqCheckout.text.toString()

        sharedPreferences.edit {
            putString(SharePrefKeys.SAVE_TIME_CHECKOUT.data, requestTimeCheckOut)
        }

    }

}