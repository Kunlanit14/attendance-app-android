package com.example.attendanceapp.fragment.checkIncheckOut

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
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys

class CheckInOutFragment : Fragment() {

    //initial variables
    lateinit var checkInButton : Button
    lateinit var checkOutButton : Button
    lateinit var checkIn : TextView
    lateinit var checkOut : TextView

    //Container stored pref
    var time: String? = null
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

        //Date
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate : String = dateFormat.format(Date())

        checkOutButton.visibility = View.GONE
        checkInButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime : String = simpleDateFormat.format(Date().time)

            checkIn.text = getString(R.string.check_in_at,currentTime)
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
            saveButtonState("out")
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
                putString("timeCheckIn", currentTime)
                putString(ActivityLogKeyEnum.CHECK_IN_LIST.key, gson.toJson(checkInDataList))
            }

        }

        checkOutButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTimeCheckOut : String = simpleDateFormat.format(Date().time)

            checkOut.text = getString(R.string.check_out_at,currentTimeCheckOut)
            checkOutButton.visibility = View.GONE
            checkInButton.visibility = View.VISIBLE
            saveButtonState("in")
            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

            //

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
                putString("timeCheckOut", currentTimeCheckOut)
                putString(ActivityLogKeyEnum.CHECK_OUT_LIST.key, gson.toJson(checkOutdataList))
            }
        }

        return view

    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        retreiveData()

        val buttonState = loadSaveButtonState()

        if(buttonState == "in"){
            checkInButton.visibility = View.VISIBLE
            checkOutButton.visibility = View.GONE
        }else {
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
        }

        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        val receiveTimeCheckIn = sharedPreferences.getString("timeCheckIn","")
        val receiveTimeCheckOut = sharedPreferences.getString("timeCheckOut","")


        if(!receiveTimeCheckIn.isNullOrEmpty()) {
            checkIn.text = getString(R.string.check_in_at,receiveTimeCheckIn)
        }

        if(!receiveTimeCheckOut.isNullOrEmpty()) {
            checkOut.text = getString(R.string.check_out_at,receiveTimeCheckOut)
        }

    }


    fun saveData(){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        time = checkIn.text.toString()
        timeCheckOut = checkOut.text.toString()

        sharedPreferences.edit {
            putString("key time", time)
            putString("key timeCheckOut", timeCheckOut)
        }

    }

    fun retreiveData(){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)

        time = sharedPreferences.getString("key time",getString(R.string.check_in_at_text))
        timeCheckOut = sharedPreferences.getString("key timeCheckOut",getString(R.string.check_out_at_text))

        checkIn.text = time
        checkOut.text = timeCheckOut

    }

    fun saveButtonState(state: String){
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(ButtonEnum.BUTTON_STATE.state, state)
        }

    }


    fun loadSaveButtonState() : String {
        sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ButtonEnum.BUTTON_STATE.state,"in") ?: "in"
    }
}