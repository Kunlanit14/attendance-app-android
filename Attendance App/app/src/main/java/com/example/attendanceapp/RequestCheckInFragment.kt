package com.example.attendanceapp

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class RequestCheckInFragment : Fragment() {

    lateinit var saveButton : Button
    lateinit var time: TextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request_check_in, container, false)

        saveButton = view.findViewById(R.id.btnSave)
        time = view.findViewById(R.id.tvTime)


        saveButton.setOnClickListener {
            val timReplace = time.text.toString()
            val budle = Bundle()
            budle.putString("time", timReplace)

            val checkInOutFragment = CheckInOutFragment()
            checkInOutFragment.arguments = budle


            val fragmentManager : FragmentManager = requireActivity().supportFragmentManager

            val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.checkInOutFrame, checkInOutFragment)
            fragmentTransaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }



}