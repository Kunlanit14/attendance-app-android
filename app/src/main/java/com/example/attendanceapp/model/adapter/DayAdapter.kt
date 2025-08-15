package com.example.attendanceapp.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.dto.data.DayData

class DayAdapter(private val days: List<DayData>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

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
        val day = days[position]
        holder.dayText.text = day.dayNumber.toString()

        if(day.isToday) {
            holder.dayText.setBackgroundResource(R.drawable.calendar_today_marker)
        }else {
            holder.dayText.background = null
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }


}