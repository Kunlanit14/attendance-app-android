package com.example.attendanceapp.fragment.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.ButtonEnum
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.data.model.CheckInData
import com.example.attendanceapp.data.model.CheckOutData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys

class CheckInOutFragment : Fragment() {

    //initial variables
    lateinit var checkInButton : Button
    lateinit var checkOutButton : Button
    lateinit var checkIn : TextView
    lateinit var checkOut : TextView

    //Container stored pref
    var timeCheckIn: String? = null
    var timeCheckOut: String? = null

    lateinit var sharedPreferences: SharedPreferences

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

        saveCheckIn()
        saveCheckOut()

        return view

    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        retreiveData()
        handleButtonState()
        handleRetrieveTimeChecked()
    }

    fun saveCheckIn(){

        val currentDate : String = getCurrentDate()
        val currentTime : String = getCurrentTime()

        checkOutButton.visibility = View.GONE
        checkInButton.setOnClickListener {

            checkIn.text = getString(R.string.check_in_at,currentTime)
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
            saveButtonState(ButtonEnum.BUTTON_STATE_OUT.state)

            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

            //List for store many data in Tablelayout
            val gson = Gson()
            val json = sharedPreferences.getString(ActivityLogKeyEnum.CHECK_IN_LIST.key,null)
            val type = object : TypeToken<MutableList<CheckInData>>(){}.type
            val checkInDataList : MutableList<CheckInData> = if (json != null) {
                gson.fromJson(json,type)
            }else{
                mutableListOf()
            }

            val newData = CheckInData(
                checkInTime = currentTime,
                dateCheckIn = currentDate,
                requestType = RequestTypeEnum.CHECK_IN.type
            )
            checkInDataList.add(newData)

            sharedPreferences.edit {
                putString(SharePrefKeys.TIME_CHECKIN.data, currentTime)
                putString(ActivityLogKeyEnum.CHECK_IN_LIST.key, gson.toJson(checkInDataList))
            }

        }
    }

    fun saveCheckOut(){

        val currentDate : String = getCurrentDate()
        val currentTimeCheckOut : String = getCurrentTime()
        checkOutButton.setOnClickListener {
            checkOut.text = getString(R.string.check_out_at,currentTimeCheckOut)
            checkOutButton.visibility = View.GONE
            checkInButton.visibility = View.VISIBLE
            saveButtonState(ButtonEnum.BUTTON_STATE_IN.state)
            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

            val gson = Gson()
            val jsonCheckOut = sharedPreferences.getString(ActivityLogKeyEnum.CHECK_OUT_LIST.key,null)
            val typeCheckOut = object : TypeToken<MutableList<CheckOutData>>(){}.type
            val checkOutdataList : MutableList<CheckOutData> = if (jsonCheckOut != null){
                gson.fromJson(jsonCheckOut, typeCheckOut)
            }else {
                mutableListOf()
            }

            val newCheckOutData = CheckOutData(
                checkOutTime = currentTimeCheckOut,
                dateCheckOut = currentDate,
                requestType = RequestTypeEnum.CHECK_OUT.type
            )

            checkOutdataList.add(newCheckOutData)

            sharedPreferences.edit {
                putString(SharePrefKeys.TIME_CHECKOUT.data, currentTimeCheckOut)
                putString(ActivityLogKeyEnum.CHECK_OUT_LIST.key, gson.toJson(checkOutdataList))
            }
        }
    }

    fun saveData(){

        val currentTime : String = getCurrentTime()
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        timeCheckIn = currentTime
        timeCheckOut = currentTime

        sharedPreferences.edit {
            putString(SharePrefKeys.TIME_CHECKIN.data, timeCheckIn)
            putString(SharePrefKeys.TIME_CHECKOUT.data, timeCheckOut)
        }

    }

    fun retreiveData(){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        timeCheckIn = sharedPreferences.getString(SharePrefKeys.TIME_CHECKIN.data,getString(R.string.check_in_at_text))
        timeCheckOut = sharedPreferences.getString(SharePrefKeys.TIME_CHECKOUT.data,getString(R.string.check_out_at_text))

        checkIn.text = timeCheckIn
        checkOut.text = timeCheckOut

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

    fun handleRetrieveTimeChecked(){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        val receiveTimeCheckIn = sharedPreferences.getString(SharePrefKeys.TIME_CHECKIN.data,"")
        val receiveTimeCheckOut = sharedPreferences.getString(SharePrefKeys.TIME_CHECKOUT.data,"")

        if(!receiveTimeCheckIn.isNullOrEmpty()) {
            checkIn.text = getString(R.string.check_in_at,receiveTimeCheckIn)
        }

        if(!receiveTimeCheckOut.isNullOrEmpty()) {
            checkOut.text = getString(R.string.check_out_at,receiveTimeCheckOut)
        }
    }

    fun loadSaveButtonState() : String {
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ButtonEnum.BUTTON_STATE.state, ButtonEnum.BUTTON_STATE_IN.state) ?: ButtonEnum.BUTTON_STATE_IN.state
    }

    fun getCurrentDate() : String {
        //Date
        val dateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getCurrentTime() : String {
        //Date
        val simpleTimeFormat = SimpleDateFormat(DateTimeFormat.TIME_PATTERN.format, Locale.getDefault())
        return simpleTimeFormat.format(Date().time)
    }
}