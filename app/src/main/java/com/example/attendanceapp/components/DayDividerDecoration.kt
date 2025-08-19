package com.example.attendanceapp.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class DayDividerDecoration(
    private val dividerSize: Int = 1,
    private val dividerColor: Int = Color.LTGRAY
    ) : RecyclerView.ItemDecoration() {

        private val paint = Paint().apply {
            color = dividerColor
            style = Paint.Style.FILL
        }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
       val childCount = parent.childCount

        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            //Horizontal line
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerSize
            c.drawRect(parent.left.toFloat(),top.toFloat(),parent.right.toFloat(),bottom.toFloat(),paint)
        }
    }

}