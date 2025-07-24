package com.example.attendanceapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.fragment.activityLog.ActivityLogFragment
import com.example.attendanceapp.fragment.home.CheckInOutFragment
import com.example.attendanceapp.fragment.request.RequestFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {


    lateinit var dateAndTime : TextView
    lateinit var menuButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.Companion.dark(1))
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }


        dateAndTime = findViewById(R.id.dateAndTime)
        menuButton = findViewById(R.id.menuButton)

        //get current date
        val simpleDateFormat = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
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
            val popUpMenu = PopupMenu(ContextThemeWrapper(this,R.style.AppPopupMenuStyle),menuButton)
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
                    R.id.activities_log -> {
                        val fm : FragmentManager = supportFragmentManager
                        val ft : FragmentTransaction = fm.beginTransaction()
                        val activityLogFragment = ActivityLogFragment()

                        ft.replace(R.id.checkInOutFrame,activityLogFragment)
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