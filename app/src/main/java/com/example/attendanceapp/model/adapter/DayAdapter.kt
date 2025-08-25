package com.example.attendanceapp.model.adapter

import android.graphics.Color
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.dto.data.DayData

class DayAdapter(private val days: List<DayData>,
    private val monthName: String,
    private val year: String,
    private val onDateSelected: (String, DayData) -> Unit) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val dayText: TextView = view.findViewById(R.id.dayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_item,parent,false)
        // ส่ง View เข้าไปใน ViewHolder ข้างในก็จะหา findViewById และเก็บ dayText ไว้
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        var day = days[position]
        holder.dayText.text = day.dayNumber.toString()

        if(day.dayNumber == 0){
            holder.dayText.text = "" // ช่องว่างก่อนวันที่ 1
            holder.dayText.background = null
        }

        if(day.isWeekend) {
            holder.dayText.setTextColor(Color.GRAY)
        }else {
            holder.dayText.setTextColor(Color.BLACK)
        }

        if (day.dayNumber != 0) {
            when {
                day.isToday && day.isSelected -> {
                    holder.dayText.setBackgroundResource(R.drawable.bg_today_selected)
                    holder.dayText.setTextColor(Color.WHITE)
                }

                day.isToday -> {
                    holder.dayText.setBackgroundResource(R.drawable.calendar_select_marker_btn)
                }

                day.isSelected -> {
                    holder.dayText.setBackgroundResource(R.drawable.calendar_today_marker)
                    holder.dayText.setTextColor(Color.WHITE)
                }

                else -> {
                    holder.dayText.background = null
                }
            }

            //Selected date
            holder.dayText.setOnClickListener {
                //Clear selected date -> for select 1 only
                days.forEach {
                    it.isSelected = false
                }
                day.isSelected = true

                val selectedDate = "${day.dayNumber} $monthName, $year"
                notifyDataSetChanged()
                onDateSelected(selectedDate,day)

            }
        } else {
           holder.dayText.setOnClickListener(null)
        }

    }

    override fun getItemCount(): Int {
        return days.size
    }


}