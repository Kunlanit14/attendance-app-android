package com.example.attendanceapp.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MonthDayDividerDecoration(
    private val dividerSize: Int = 2,
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

            //Horizontal line
            val top = child.bottom.toFloat()
            val bottom = top + dividerSize
            c.drawRect(0f,top,parent.width.toFloat(),bottom,paint)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerSize
    }

}