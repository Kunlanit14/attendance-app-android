package com.example.attendanceapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.attendanceapp.data.ActivitiesLogCommonItem
import com.example.attendanceapp.data.CheckInData
import com.example.attendanceapp.data.CheckOutData
import com.example.attendanceapp.data.RequestCheckInData
import com.example.attendanceapp.data.RequestCheckOutData
import com.example.attendanceapp.data.RequestLeaveData
import com.example.attendanceapp.data.RequestOTData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text

class ActivityLogFragment : Fragment() {

    lateinit var tableActivities : TableLayout
    lateinit var sharedPreferences: SharedPreferences

    val CHECK_IN_LIST_KEY = "check_in_list"
    val CHECK_OUT_LIST_KEY = "check_out_list"
    val REQUEST_OT_LIST = "request_ot_list"
    val REQUEST_CHECKIN_LIST = "request_checkin_list"
    val REQUEST_CHECKOUT_LIST = "request_checkout_list"
    val REQUEST_LEAVE_LIST = "request_leave_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_log, container, false)

        tableActivities = view.findViewById(R.id.tbActivitiesLog)


        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()


        val headerRow = tableActivities.getChildAt(0)
        tableActivities.removeAllViews()
        tableActivities.addView(headerRow)


        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(CHECK_IN_LIST_KEY,null)
        val type = object : TypeToken<List<CheckInData>>(){}.type
        val checkInList : List<CheckInData> = if (json != null) {
            Gson().fromJson(json, type)
         }else {
            mutableListOf()
        }
        //Check-in

        val checkInItem = checkInList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateCheckIn,
                requestType = "Check-in",
                details = "Check-in : ${it.checkInTime}"
            )
        }

        //Check-out
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonCheckOut = sharedPreferences.getString(CHECK_OUT_LIST_KEY,null)
        val typeCheckOut = object : TypeToken<List<CheckOutData>>(){}.type
        val checkOutList : List<CheckOutData> = if (jsonCheckOut != null) {
            Gson().fromJson(jsonCheckOut, typeCheckOut)
        }else {
            mutableListOf()
        }

        val checkOutItem = checkOutList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateCheckOut,
                requestType = "Check-out",
                details = "Check-out : ${it.checkOutTime}"
            )
        }

        //Request Check-in
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonReqCheckIn = sharedPreferences.getString(REQUEST_CHECKIN_LIST,null)
        val typeReqCheckIn = object : TypeToken<List<RequestCheckInData>>(){}.type
        val requestCheckInList : List<RequestCheckInData> = if (jsonReqCheckIn != null){
            Gson().fromJson(jsonReqCheckIn, typeReqCheckIn)
        } else {
            mutableListOf()
        }

        val requestCheckInItem = requestCheckInList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateReqCheckIn,
                requestType = "Request Check-in",
                details = "Request Check-in Date :\n${it.dateReqCheckIn}\nTime : ${it.requestCheckInTime}"
            )
        }

        //Request Check-out
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonReqCheckOut = sharedPreferences.getString(REQUEST_CHECKOUT_LIST,null)
        val typeReqCheckOut = object : TypeToken<List<RequestCheckOutData>>(){}.type
        val requestCheckOutList : List<RequestCheckOutData> = if (jsonReqCheckOut != null){
            Gson().fromJson(jsonReqCheckOut, typeReqCheckOut)
        } else {
            mutableListOf()
        }

        val requestCheckOutItem = requestCheckOutList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateReqCheckOut,
                requestType = "Request Check-out",
                details = "Request Check-out Date :\n${it.dateReqCheckOut}\nTime : ${it.requestCheckOutTime}"
            )
        }


        //Request OT
        val jsonOT = sharedPreferences.getString(REQUEST_OT_LIST,null)
        val typeOT = object : TypeToken<List<RequestOTData>>(){}.type
        val requestOTList : List<RequestOTData> = if (jsonOT != null) {
            Gson().fromJson(jsonOT, typeOT)
        }else {
            mutableListOf()
        }

        val requestCheckOTItem = requestOTList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.otDateRequest,
                requestType = "Request OT",
                details = "OT Date : ${it.otDateRequest}\nFrom Time : ${it.fromTime}\nTo Time : ${it.toTime}\nReason : ${it.reasonOT}"
            )
        }


        //Request Leave
        val jsonLeave = sharedPreferences.getString(REQUEST_LEAVE_LIST,null)
        val typeLeave = object : TypeToken<List<RequestLeaveData>>(){}.type
        val requestLeaveList : List<RequestLeaveData> = if (jsonLeave != null) {
            Gson().fromJson(jsonLeave, typeLeave)
        }else {
            mutableListOf()
        }

        val requestCheckLeaveItem = requestLeaveList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.currentDate,
                requestType = "Request Leave",
                details = "Leave Type : ${it.leaveType}\nFrom Date : ${it.fromDate}\nTo Date : ${it.toDate}\nPeriod : ${it.period}\nReason : ${it.reasonLeave}"
            )
        }

        val allLogItems = (checkInItem + checkOutItem + requestCheckInItem + requestCheckOutItem + requestCheckOTItem + requestCheckLeaveItem)
            .sortedWith(compareBy({it.dateRequest}, {
                when (it.requestType) {
                    "Check-in" -> 1
                    "Request Check-in" -> 2
                    "Check-out" -> 3
                    "Request Check-out" -> 4
                    "Request OT" -> 5
                    "Request Leave" -> 6
                    else -> ""
                }
            }))




        for ((index,logItem) in allLogItems.withIndex()) {


            val row = TableRow(requireContext())

            if (index % 2 == 0){
                row.setBackgroundColor(Color.parseColor("#CDD3E8"))
            } else {
                row.setBackgroundColor(Color.parseColor("#E7E9F3"))
            }
            
            val dateTextView = TextView(requireContext())
            dateTextView.text = logItem.dateRequest
            dateTextView.textSize = 11.5f
            dateTextView.setTextColor(Color.BLACK)
            dateTextView.setPadding(10, 10, 10, 10)

            val requestTypeTextView = TextView(requireContext())
            requestTypeTextView.text = logItem.requestType
            requestTypeTextView.textSize = 11.5f
            requestTypeTextView.setTextColor(Color.BLACK)
            requestTypeTextView.setPadding(10, 10, 10, 10)

            val detailTextView = TextView(requireContext())
            detailTextView.text = logItem.details
            detailTextView.textSize = 11.5f
            detailTextView.setTextColor(Color.BLACK)
            detailTextView.setPadding(0,10, 10, 10)

            row.addView(dateTextView)
            row.addView(requestTypeTextView)
            row.addView(detailTextView)

            tableActivities.addView(row)
        }

    }

}