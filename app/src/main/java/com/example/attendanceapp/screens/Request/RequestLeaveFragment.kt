package com.example.attendanceapp.screens.Request

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isNotEmpty
import com.example.attendanceapp.R
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.RequestLeaveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date

class RequestLeaveFragment : Fragment() {

    lateinit var btnCalendarFromDate : ImageView
    lateinit var btnCalendarToDate : ImageView
    lateinit var etFromDate : TextView
    lateinit var etToDate : TextView
    lateinit var leaveTypeSpinner : Spinner
    lateinit var periodSpinner : Spinner
    lateinit var btnLeaveSave : Button
    lateinit var btnLeaveCancel : Button
    lateinit var etLeaveReason : EditText
    lateinit var sharedPreferences : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request_leave, container, false)

        btnCalendarFromDate = view.findViewById(R.id.imgCalendarFromDate)
        btnCalendarToDate = view.findViewById(R.id.imgCalendarToDate)
        etFromDate = view.findViewById(R.id.etFromDateLeave)
        etToDate = view.findViewById(R.id.etToDateLeave)
        leaveTypeSpinner = view.findViewById(R.id.spinnerLeaveType)
        periodSpinner = view.findViewById(R.id.spinnerPeriod)
        btnLeaveSave = view.findViewById(R.id.btnSaveLeave)
        btnLeaveCancel = view.findViewById(R.id.btnCancelLeave)
        etLeaveReason = view.findViewById(R.id.etReasonLeave)

        val REQUEST_LEAVE_LIST = "request_leave_list"

        btnCalendarFromDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etFromDate.text = selectedDate
            }
        }

        btnCalendarToDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etToDate.text = selectedDate
            }
        }

        //Spinner Leave Type
        val leaveTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.LeaveType,
            android.R.layout.simple_spinner_dropdown_item
        )

        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leaveTypeSpinner.adapter = leaveTypeAdapter

        //Handle leave selection
        leaveTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val selectedItem = parent?.getItemAtPosition(position).toString()
//                when (selectedItem){
//                    "Annual (P)" -> {
//
//                    }
//                }
                updateButtonStateLeave()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        //Spinner Period Type
        val periodAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Period,
            android.R.layout.simple_spinner_dropdown_item
        )

        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        periodSpinner.adapter = periodAdapter

        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateButtonStateLeave()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        btnLeaveSave.isEnabled = false
        btnLeaveSave.setOnClickListener {
            Toast.makeText(requireContext(), "Request Leave was saved.", Toast.LENGTH_LONG).show()

            //Date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy")
            val currentDate : String = dateFormat.format(Date())
            sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(REQUEST_LEAVE_LIST,null)
            val type = object : TypeToken<MutableList<RequestLeaveData>>(){}.type
            val requestLeaveDataList : MutableList<RequestLeaveData> = if (json != null) {
                gson.fromJson(json, type)
            } else {
                mutableListOf()
            }

            val newRequestLeaveData = RequestLeaveData(
                currentDate = currentDate,
                requestType = "Request-Leave",
                leaveType = leaveTypeSpinner.selectedItem.toString(),
                fromDate = etFromDate.text.toString(),
                toDate = etToDate.text.toString(),
                reasonLeave = etLeaveReason.text.toString(),
                period = periodSpinner.selectedItem.toString()
            )
            requestLeaveDataList.add(newRequestLeaveData)




            val editor = sharedPreferences.edit()
            editor.putString(REQUEST_LEAVE_LIST,gson.toJson(requestLeaveDataList))
            editor.putString("leaveType",leaveTypeSpinner.toString())
            editor.putString("fromDateLeave",etFromDate.text.toString())
            editor.putString("toDateLeave",etToDate.text.toString())
            editor.putString("period",periodSpinner.toString())
            editor.putString("reasonLeave",etLeaveReason.text.toString())
            editor.apply()


            leaveTypeSpinner.setSelection(0)
            periodSpinner.setSelection(0)
            etFromDate.text = ""
            etToDate.text = ""
            etLeaveReason.text.clear()


        }

        btnLeaveCancel.setOnClickListener {
            leaveTypeSpinner.setSelection(0)
            periodSpinner.setSelection(0)
            etFromDate.text = ""
            etToDate.text = ""
            etLeaveReason.text.clear()
        }

        etFromDate.addTextChangedListener( object : TextWatcher {
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
                updateButtonStateLeave()
            }

        })

        etToDate.addTextChangedListener( object : TextWatcher {
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
                updateButtonStateLeave()
            }

        })

        etLeaveReason.addTextChangedListener( object : TextWatcher {
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
                updateButtonStateLeave()
            }

        })

        leaveTypeSpinner.onItemSelectedListener

        // Inflate the layout for this fragment
        return view
    }

    fun updateButtonStateLeave(){
        val selectedLeaveType = leaveTypeSpinner.isNotEmpty()
        val selectedPeriodType = periodSpinner.isNotEmpty()
        val fromDateLeave = etFromDate.text.toString()
        val toDateLeave = etToDate.text.toString()
        val reasonLeave = etLeaveReason.text.toString()

        val enabledSaveLeave = selectedLeaveType && selectedPeriodType && fromDateLeave.isNotEmpty() && toDateLeave.isNotEmpty() && reasonLeave.isNotEmpty()

        btnLeaveSave.isEnabled = enabledSaveLeave
        if(enabledSaveLeave){
            btnLeaveSave.setBackgroundResource(R.drawable.button_save)
        }else{
            btnLeaveSave.setBackgroundResource(R.drawable.button_save_disable)
        }
    }


}