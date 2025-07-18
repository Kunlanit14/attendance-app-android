package com.example.attendanceapp.screens.activityLog

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.data.model.ActivitiesLogCommonItem
import com.example.attendanceapp.data.model.CheckInData
import com.example.attendanceapp.data.model.CheckOutData
import com.example.attendanceapp.data.model.RequestCheckInData
import com.example.attendanceapp.data.model.RequestCheckOutData
import com.example.attendanceapp.data.model.RequestLeaveData
import com.example.attendanceapp.data.model.RequestOTData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.graphics.toColorInt

class ActivityLogFragment : Fragment() {

    lateinit var tableActivities : TableLayout
    lateinit var sharedPreferences: SharedPreferences

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
        val json = sharedPreferences.getString(ActivityLogKeyEnum.CHECK_IN_LIST.key,null)
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
                requestType = RequestTypeEnum.CHECK_IN.type,
                details = "Check-in : ${it.checkInTime}"
            )
        }

        //Check-out
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonCheckOut = sharedPreferences.getString(ActivityLogKeyEnum.CHECK_OUT_LIST.key,null)
        val typeCheckOut = object : TypeToken<List<CheckOutData>>(){}.type
        val checkOutList : List<CheckOutData> = if (jsonCheckOut != null) {
            Gson().fromJson(jsonCheckOut, typeCheckOut)
        }else {
            mutableListOf()
        }

        val checkOutItem = checkOutList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateCheckOut,
                requestType = RequestTypeEnum.CHECK_OUT.type,
                details = "Check-out : ${it.checkOutTime}"
            )
        }

        //Request Check-in
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonReqCheckIn = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_CHECKIN_LIST.key,null)
        val typeReqCheckIn = object : TypeToken<List<RequestCheckInData>>(){}.type
        val requestCheckInList : List<RequestCheckInData> = if (jsonReqCheckIn != null){
            Gson().fromJson(jsonReqCheckIn, typeReqCheckIn)
        } else {
            mutableListOf()
        }

        val requestCheckInItem = requestCheckInList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateReqCheckIn,
                requestType = RequestTypeEnum.REQUEST_CHECK_IN.type,
                details = "Request Check-in Date :\n${it.dateReqCheckIn}\nTime : ${it.requestCheckInTime}"
            )
        }

        //Request Check-out
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val jsonReqCheckOut = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_CHECKOUT_LIST.key,null)
        val typeReqCheckOut = object : TypeToken<List<RequestCheckOutData>>(){}.type
        val requestCheckOutList : List<RequestCheckOutData> = if (jsonReqCheckOut != null){
            Gson().fromJson(jsonReqCheckOut, typeReqCheckOut)
        } else {
            mutableListOf()
        }

        val requestCheckOutItem = requestCheckOutList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.dateReqCheckOut,
                requestType = RequestTypeEnum.REQUEST_CHECK_OUT.type,
                details = "Request Check-out Date :\n${it.dateReqCheckOut}\nTime : ${it.requestCheckOutTime}"
            )
        }


        //Request OT
        val jsonOT = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_OT_LIST.key,null)
        val typeOT = object : TypeToken<List<RequestOTData>>(){}.type
        val requestOTList : List<RequestOTData> = if (jsonOT != null) {
            Gson().fromJson(jsonOT, typeOT)
        }else {
            mutableListOf()
        }

        val requestCheckOTItem = requestOTList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.otDateRequest,
                requestType = RequestTypeEnum.REQUEST_OT.type,
                details = "OT Date : ${it.otDateRequest}\nFrom Time : ${it.fromTime}\nTo Time : ${it.toTime}\nReason : ${it.reasonOT}"
            )
        }


        //Request Leave
        val jsonLeave = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_LEAVE_LIST.key,null)
        val typeLeave = object : TypeToken<List<RequestLeaveData>>(){}.type
        val requestLeaveList : List<RequestLeaveData> = if (jsonLeave != null) {
            Gson().fromJson(jsonLeave, typeLeave)
        }else {
            mutableListOf()
        }

        val requestCheckLeaveItem = requestLeaveList.map {
            ActivitiesLogCommonItem(
                dateRequest = it.currentDate,
                requestType = RequestTypeEnum.REQUEST_LEAVE.type,
                details = "Leave Type : ${it.leaveType}\nFrom Date : ${it.fromDate}\nTo Date : ${it.toDate}\nPeriod : ${it.period}\nReason : ${it.reasonLeave}"
            )
        }

        val allLogItems = (checkInItem + checkOutItem + requestCheckInItem + requestCheckOutItem + requestCheckOTItem + requestCheckLeaveItem)
            .sortedWith(compareBy({it.dateRequest}, {
                when (it.requestType) {
                    RequestTypeEnum.CHECK_IN.type -> 1
                    RequestTypeEnum.REQUEST_CHECK_IN.type -> 2
                    RequestTypeEnum.CHECK_OUT.type -> 3
                    RequestTypeEnum.REQUEST_CHECK_OUT.type -> 4
                    RequestTypeEnum.REQUEST_OT.type -> 5
                    RequestTypeEnum.REQUEST_LEAVE.type -> 6
                    else -> ""
                }
            }))




        for ((index,logItem) in allLogItems.withIndex()) {


            val row = TableRow(requireContext())

            if (index % 2 == 0){
                row.setBackgroundColor("#CDD3E8".toColorInt())
            } else {
                row.setBackgroundColor("#E7E9F3".toColorInt())
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