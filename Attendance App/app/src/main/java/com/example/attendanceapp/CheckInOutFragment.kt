package com.example.attendanceapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.transition.Visibility
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date

class CheckInOutFragment : Fragment() {

    lateinit var checkInButton : Button
    lateinit var checkOutButton : Button
    lateinit var checkIn : TextView
    lateinit var checkOut : TextView


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


        checkOutButton.visibility = View.GONE
        checkInButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val currentTime : String = simpleDateFormat.format(Date().time)
            checkIn.text = "Check in at $currentTime"
            checkInButton.visibility = View.GONE
            checkOutButton.visibility = View.VISIBLE
        }

        checkOutButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val currentTimeCheckOut : String = simpleDateFormat.format(Date().time)
            checkOut.text = "Check out at $currentTimeCheckOut"
            checkOutButton.visibility = View.GONE
            checkInButton.visibility = View.VISIBLE
        }

        checkIn.text = arguments?.getString("time").toString()
        checkOut.text = arguments?.getString("time").toString()



//        val timeReplace : TextView = view.findViewById(R.id.tvCheckIn)
//        timeReplace.text = arguments?.getInt("time").toString()
//        checkInButton.visibility = View.GONE
//        checkOutButton.setOnClickListener {
//            checkOut.text = "Check out at $currentTime"
//            checkOutButton.visibility = View.GONE
//            checkInButton.visibility = View.VISIBLE
//        }

        return view

    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        retreiveData()

    }


    fun saveData(){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        time = checkIn.text.toString()
        timeCheckOut = checkOut.text.toString()

        val editor = sharedPreferences.edit()

        editor.putString("key time",time)
        editor.putString("Key timeChekOut",timeCheckOut)
        editor.apply()

    }

    fun retreiveData(){
        sharedPreferences = requireActivity().getSharedPreferences("saveData", Context.MODE_PRIVATE)

        time = sharedPreferences.getString("key time","Check in at xx:xx")
        timeCheckOut = sharedPreferences.getString("key timeChekOut","Check out at xx:xx")

        checkIn.setText(time)
        checkOut.setText(timeCheckOut)


    }

}
