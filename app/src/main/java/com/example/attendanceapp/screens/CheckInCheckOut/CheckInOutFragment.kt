package com.example.attendanceapp.screens.CheckInCheckOut

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.attendanceapp.R
import com.example.attendanceapp.data.CheckInData
import com.example.attendanceapp.data.CheckOutData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date

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

    //KEYS
    val KEY_BUTTON_STATE = "button_state"


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


        val CHECK_IN_LIST_KEY = "check_in_list"
        val CHECK_OUT_LIST_KEY = "check_out_list"
        //Date
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")
        val currentDate : String = dateFormat.format(Date())

        checkOutButton.visibility = View.GONE
        checkInButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val currentTime : String = simpleDateFormat.format(Date().time)

            checkIn.text = "Check in at $currentTime"
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
            saveButtonState("out")
            sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)


            //List for store many data in Tablelayout
            val gson = Gson()
            val json = sharedPreferences.getString(CHECK_IN_LIST_KEY,null)
            val type = object : TypeToken<MutableList<CheckInData>>(){}.type
            val checkInDataList : MutableList<CheckInData> = if (json != null) {
                gson.fromJson(json,type)
            }else{
                mutableListOf()
            }

            val newData = CheckInData(
                checkInTime = currentTime,
                dateCheckIn = currentDate,
                requestType = "Check-in"
            )
            checkInDataList.add(newData)

            val editor = sharedPreferences.edit()
            editor.putString("timeCheckIn",currentTime)
            editor.putString(CHECK_IN_LIST_KEY,gson.toJson(checkInDataList))
            editor.apply()

        }

        checkOutButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val currentTimeCheckOut : String = simpleDateFormat.format(Date().time)

            checkOut.text = "Check out at $currentTimeCheckOut"
            checkOutButton.visibility = View.GONE
            checkInButton.visibility = View.VISIBLE
            saveButtonState("in")
            sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

            //

            val gson = Gson()
            val jsonCheckOut = sharedPreferences.getString(CHECK_OUT_LIST_KEY,null)
            val typeCheckOut = object : TypeToken<MutableList<CheckOutData>>(){}.type
            val checkOutdataList : MutableList<CheckOutData> = if (jsonCheckOut != null){
                gson.fromJson(jsonCheckOut, typeCheckOut)
            }else {
                mutableListOf()
            }

            val newCheckOutData = CheckOutData(
                checkOutTime = currentTimeCheckOut,
                dateCheckOut = currentDate,
                requestType = "Check-out"
            )

            checkOutdataList.add(newCheckOutData)


            val editor = sharedPreferences.edit()
            editor.putString("timeCheckOut",currentTimeCheckOut)
            editor.putString(CHECK_OUT_LIST_KEY,gson.toJson(checkOutdataList))
            editor.apply()
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

        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val receiveTimeCheckIn = sharedPreferences.getString("timeCheckIn","")
        val receiveTimeCheckOut = sharedPreferences.getString("timeCheckOut","")


        if(!receiveTimeCheckIn.isNullOrEmpty()) {
            checkIn.text = "Check in at $receiveTimeCheckIn"
        }

        if(!receiveTimeCheckOut.isNullOrEmpty()) {
            checkOut.text = "Check out at $receiveTimeCheckOut"
        }

    }


    fun saveData(){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        time = checkIn.text.toString()
        timeCheckOut = checkOut.text.toString()



        Log.d("SharePrefTime", "Time receive: $time")
        Log.d("SharePrefTimeCheckOut", "Time receive: $timeCheckOut")
        val editor = sharedPreferences.edit()

        editor.putString("key time",time)
        editor.putString("key timeCheckOut",timeCheckOut)
        editor.apply()

    }

    fun retreiveData(){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        time = sharedPreferences.getString("key time","Check in at xx:xx")
        timeCheckOut = sharedPreferences.getString("key timeCheckOut","Check out at xx:xx")

        checkIn.setText(time)
        checkOut.setText(timeCheckOut)

    }

    fun saveButtonState(state: String){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(KEY_BUTTON_STATE,state)
        editor.apply()

    }


    fun loadSaveButtonState() : String {
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_BUTTON_STATE,"in") ?: "in"
    }
}