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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.example.attendanceapp.R
import com.example.attendanceapp.components.CalendarPicker
import com.example.attendanceapp.data.model.RequestLeaveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import com.example.attendanceapp.common.constant.ActivityLogKeyEnum
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.common.constant.RequestTypeEnum
import com.example.attendanceapp.common.shareprefkeys.SharePrefKeys

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


        handleCalendarFromDate()
        handleCalendarToDate()
        spinnerLeaveTypeAdapter()
        spinnerPeriodAdapter()
        saveDataRequestLeave()
        clearDataRequestLeave()
        handleFromDateTextChanged()
        handleToDateTextChanged()
        handleReasonTextChanged()

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

    fun parsedDateFormat(dateString: String) : String {
        val inputFormat = SimpleDateFormat(DateTimeFormat.PARSE_DATE_PATTERN.format, Locale.getDefault())
        val outputFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())

        val date = inputFormat.parse(dateString)

        return date?.let { outputFormat.format(it) }?: ""

    }

    fun handleCalendarFromDate(){
        btnCalendarFromDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etFromDate.text = selectedDate
            }
        }
    }

    fun handleCalendarToDate(){
        btnCalendarToDate.setOnClickListener {
            CalendarPicker.showDatePicker(requireContext()) {
                    selectedDate ->
                etToDate.text = selectedDate
            }
        }
    }

    fun spinnerLeaveTypeAdapter(){
        //Spinner Leave Type
        val leaveTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.LeaveType,
            android.R.layout.simple_spinner_dropdown_item
        )

        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leaveTypeSpinner.adapter = leaveTypeAdapter
        spinnerLeaveTypeItemSelected()
    }

    fun spinnerLeaveTypeItemSelected() {
        //Handle leave selection
        leaveTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    }

    fun spinnerPeriodAdapter(){
        //Spinner Period Type
        val periodAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Period,
            android.R.layout.simple_spinner_dropdown_item
        )

        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        periodSpinner.adapter = periodAdapter
        spinnerPeriodItemSelected()
    }

    fun spinnerPeriodItemSelected() {
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
    }

    fun saveDataRequestLeave(){
        btnLeaveSave.isEnabled = false
        btnLeaveSave.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.request_leave_toast), Toast.LENGTH_LONG).show()

            //Date
            val dateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
            val currentDate : String = dateFormat.format(Date())

            val formattedFromDate = parsedDateFormat(etFromDate.text.toString())
            val formattedToDate = parsedDateFormat(etToDate.text.toString())

            sharedPreferences = requireActivity().getSharedPreferences(SharePrefKeys.SAVE_DATA.data, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(ActivityLogKeyEnum.REQUEST_LEAVE_LIST.key,null)
            val type = object : TypeToken<MutableList<RequestLeaveData>>(){}.type
            val requestLeaveDataList : MutableList<RequestLeaveData> = if (json != null) {
                gson.fromJson(json, type)
            } else {
                mutableListOf()
            }

            val newRequestLeaveData = RequestLeaveData(
                currentDate = currentDate,
                requestType = RequestTypeEnum.REQUEST_LEAVE.type,
                leaveType = leaveTypeSpinner.selectedItem.toString(),
                fromDate = formattedFromDate,
                toDate = formattedToDate,
                reasonLeave = etLeaveReason.text.toString(),
                period = periodSpinner.selectedItem.toString()
            )
            requestLeaveDataList.add(newRequestLeaveData)


            sharedPreferences.edit {
                putString(ActivityLogKeyEnum.REQUEST_LEAVE_LIST.key, gson.toJson(requestLeaveDataList))
                putString(SharePrefKeys.LEAVE_TYPE.data, leaveTypeSpinner.toString())
                putString(SharePrefKeys.FROM_DATE_LEAVE.data, etFromDate.text.toString())
                putString(SharePrefKeys.TO_DATE_LEAVE.data, etToDate.text.toString())
                putString(SharePrefKeys.PERIOD.data, periodSpinner.toString())
                putString(SharePrefKeys.REASON_LEAVE.data, etLeaveReason.text.toString())
            }

            clearDataSaved()

        }
    }

    fun clearDataRequestLeave(){
        btnLeaveCancel.setOnClickListener {
            clearDataSaved()
        }
    }

    fun clearDataSaved(){
        leaveTypeSpinner.setSelection(0)
        periodSpinner.setSelection(0)
        etFromDate.text = ""
        etToDate.text = ""
        etLeaveReason.text.clear()
    }

    fun handleFromDateTextChanged(){
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
    }

    fun handleToDateTextChanged(){
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
    }

    fun handleReasonTextChanged(){
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
    }


}