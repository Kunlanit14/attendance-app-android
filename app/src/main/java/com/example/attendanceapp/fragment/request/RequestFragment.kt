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

        spinnerAdapter()
        return view
    }

    fun spinnerAdapter(){
        var arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.RequestType,
            android.R.layout.simple_spinner_item,
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinnerItemSelected()
    }

    fun spinnerItemSelected(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectItem = parent?.getItemAtPosition(position).toString()
                val fragment = createRequestFragment(selectItem)

                fragment?.let {
                    val bundle = Bundle().apply {
                        putString(RequestTypeEnum.REQUEST_TYPE.type,selectItem)
                    }
                    replaceFragment(it,bundle)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    fun replaceFragment(fragment : Fragment,args: Bundle){
        fragment.arguments = args
        childFragmentManager.beginTransaction()
            .replace(R.id.requestFrame,fragment)
            .commit()
    }

    fun createRequestFragment(type : String) : Fragment? {
        return when (type){
            RequestTypeEnum.SPN_REQUEST_CHECKIN.type -> RequestCheckInFragment()
            RequestTypeEnum.SPN_REQUEST_CHECKOUT.type -> RequestCheckOutFragment()
            RequestTypeEnum.SPN_REQUEST_OT.type -> RequestOTFragment()
            RequestTypeEnum.SPN_REQUEST_LEAVE.type -> RequestLeaveFragment()
            else -> null
        }
    }


}
