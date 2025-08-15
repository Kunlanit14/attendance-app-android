package com.example.attendanceapp.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.components.DayDividerDecoration
import com.example.attendanceapp.model.dto.data.MonthData

//รับข้อมูลเป็น List<MonthData>(ข้อมูลที่เตรียมไว้)
//Adapter = ตัวกลางแปลงข้อมูลจากแหล่งข้อมูล MonthData : List ให้เป็นรูปแบบที่ RecyclerView สามารถแสดงผลได้
//หรือการแปลงข้อมูลให้กลายเป็น view ของแต่ละแถวหรือช่อง เพื่อทำให้ RecyclerView แสดงออกมา
class MonthAdapter(private val months: List<MonthData>) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>(){

    //จัดการเดือน 1เดือน(Viewของ 1 เดือน)
    inner class MonthViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val monthHeader: TextView = view.findViewById(R.id.monthHeader)
        val daysRecyclerView: RecyclerView = view.findViewById(R.id.daysRecyclerView)
    }

    //สร้าง View ของ RecyclerView
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthViewHolder {
        //สร้าง View จาก File
        //parent = ViewGroup(RecyclerViewแม่) ที่จะเอา view นี้ไปใส่
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_item, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthData = months[position]

        holder.monthHeader.text = monthData.monthName.toString()

        holder.daysRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.daysRecyclerView.adapter = DayAdapter(monthData.days)
        holder.daysRecyclerView.addItemDecoration(DayDividerDecoration())
    }

    override fun getItemCount(): Int {
        return months.size
    }



}