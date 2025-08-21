package com.example.attendanceapp.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.common.constant.DateTimeFormat
import com.example.attendanceapp.model.adapter.MonthAdapter
import com.example.attendanceapp.model.dto.data.DayData
import com.example.attendanceapp.model.dto.data.MonthData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CustomCalendar : AppCompatActivity() {

    lateinit var backBtn : ImageView
    lateinit var monthRecyclerView: RecyclerView
    lateinit var monthAdapter: MonthAdapter
    lateinit var monthList: List<MonthData>
    lateinit var doneBtn : Button

    private var selectDate : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.Companion.dark(1))
        setContentView(R.layout.activity_custom_calendar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
        initView()
        setUpRecyclerView()
        onPressBackListener()
        onPressDoneListener()
    }

    fun initView(){
        backBtn = findViewById(R.id.imgBack)
        monthRecyclerView = findViewById(R.id.monthRecyclerView)
        doneBtn = findViewById(R.id.doneButton)
    }

    fun onPressBackListener(){
        backBtn.setOnClickListener {

        }
    }

    fun onPressDoneListener(){
        doneBtn.setOnClickListener {
            // รูปแบบของ selectDate ปัจจุบัน
            val input = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            // แปลง String -> Date
            val date: Date = input.parse(selectDate) ?: Date()

            // ใช้ format เดียวกับ RequestCheckIn
            val sdfOutput = SimpleDateFormat(DateTimeFormat.DATE_PATTERN.format, Locale.getDefault())
            val formattedDate = sdfOutput.format(date)


            val intent = intent
            intent.putExtra("selectDate",formattedDate)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setUpRecyclerView() {
        monthList = generateCalendarData()
        monthAdapter = MonthAdapter(monthList) { date ->
            selectDate = date
        }
        monthRecyclerView.adapter = monthAdapter
        monthRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun generateCalendarData(): List<MonthData> {
        val months = mutableListOf<MonthData>()
        //ปีปัจจุบันเป็น object
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        for (month in 1..12) {
            val monthName = when(month) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> ""
            }
            val days = mutableListOf<DayData>()

            //วันที่ปัจจุบัน
            val cal = Calendar.getInstance()
            //เปลี่ยน cal เป็นวันที่ 1 ของเดือนนั้น
            cal.set(currentYear, month - 1, 1)
            //เดือนนี้มีกี่วัน
            val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            // วันที่เริ่มของเดือนนั้นๆ ตรงกับ วันอะไรใน week
            // DAY_OF_WEEK -> เพื่อหาวันในสัปดาห์ ของวันที่ 1
            val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            //ใช้ offset เพื่อวางวันที่ 1 ของเดือน
            val offset = (firstDayOfWeek + 5) % 7
            // คำนวณช่องว่างของวัน เพื่อให้เป็นวันแรกของเดือน
            for (i in 0 until offset) {
                days.add(DayData(dayNumber = 0, isToday = false))
            }

            //เริ่มวันที่จริง
            for (day in 1..maxDay) {
                cal.set(currentYear, month - 1, day)
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                //Calendar.getInstance() == วันปัจจุบัน
                 val isToday = (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
                        month == Calendar.getInstance().get(Calendar.MONTH) + 1)

                //Weekend Colors
                val isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)

                days.add(
                    DayData(
                        dayNumber = day,
                        isToday = isToday,
                        isWeekend = isWeekend
                    )
                )
            }

            //Initial Screen when no select the date
            val anySelected = days.any() {it.isSelected}
            if (!anySelected){
                days.forEach { day ->
                    if (day.isToday){
                        day.isSelected = true
                    }
                }
            }



            months.add(
                MonthData(
                    monthNumber = month,
                    monthName = monthName,
                    days = days,
                    year = currentYear.toString()
                )
            )
        }

        return months
    }



}