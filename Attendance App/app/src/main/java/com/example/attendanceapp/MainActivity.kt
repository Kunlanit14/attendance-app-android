package com.example.attendanceapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {


    lateinit var dateAndTime : TextView
    lateinit var menuButton : ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(1))
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }


        dateAndTime = findViewById(R.id.dateAndTime)
        menuButton = findViewById(R.id.menuButton)

        //get current date
        val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        val currentDateAndTime : String = simpleDateFormat.format(Date())

        dateAndTime.text = currentDateAndTime

        //Sending fragment data to actvity and displayed
        val fragementManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragementManager.beginTransaction()
        val checkInOutFragment = CheckInOutFragment()
        fragmentTransaction.add(R.id.checkInOutFrame,checkInOutFragment)
        fragmentTransaction.commit()



        //Hamburger menu
        menuButton.setOnClickListener {
            val popUpMenu = PopupMenu(this, menuButton)
            popUpMenu.menuInflater.inflate(R.menu.hamburger_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.request -> {
                        val fm : FragmentManager = supportFragmentManager
                        val ft : FragmentTransaction = fm.beginTransaction()
                        val requestFragment = RequestFragment()

                        ft.replace(R.id.checkInOutFrame,requestFragment)
                        ft.commit()
                    }
                    else -> {
                        val fragementManager : FragmentManager = supportFragmentManager
                        val fragmentTransaction : FragmentTransaction = fragementManager.beginTransaction()
                        val checkInOutFragment = CheckInOutFragment()
                        fragmentTransaction.replace(R.id.checkInOutFrame,checkInOutFragment)
                        fragmentTransaction.commit()

                    }
                }


                true
            }

            popUpMenu.show()
        }

    }



}
