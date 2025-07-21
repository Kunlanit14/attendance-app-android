package com.example.attendanceapp.fragment.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.RequestTypeEnum

class RequestFragment : Fragment() {

    lateinit var spinner : Spinner
    lateinit var tvRequestType : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_request, container, false)
        spinner = view.findViewById(R.id.spinnerRequestType)
        tvRequestType = view.findViewById(R.id.tvRequest)

        var arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.RequestType,
            android.R.layout.simple_spinner_item,
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectItem = parent?.getItemAtPosition(position).toString()

                when (selectItem){
                    RequestTypeEnum.SPN_REQUEST_CHECKIN.type -> {
                        val budle = Bundle()

                        val requestCheckInFragment = RequestCheckInFragment()
                        requestCheckInFragment.arguments = budle

                        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.requestFrame, requestCheckInFragment)
                        fragmentTransaction.commit()

                    }
                    RequestTypeEnum.SPN_REQUEST_CHECKOUT.type -> { val budle = Bundle()

                        val requestCheckOutFragment = RequestCheckOutFragment()
                        requestCheckOutFragment.arguments = budle

                        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.requestFrame,RequestCheckOutFragment() )
                        fragmentTransaction.commit()

                    }
                    RequestTypeEnum.SPN_REQUEST_OT.type -> { val budle = Bundle()

                        val requestOTFragment = RequestOTFragment()
                        requestOTFragment.arguments = budle

                        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.requestFrame,RequestOTFragment() )
                        fragmentTransaction.commit()

                    }
                    RequestTypeEnum.SPN_REQUEST_LEAVE.type -> { val budle = Bundle()

                        val requestLeaveFragment = RequestLeaveFragment()
                        requestLeaveFragment.arguments = budle

                        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.requestFrame,RequestLeaveFragment() )
                        fragmentTransaction.commit()

                    }
                    else -> ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        // Inflate the layout for this fragment
        return view
    }



}
