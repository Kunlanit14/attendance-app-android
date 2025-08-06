package com.example.attendanceapp.components

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.attendanceapp.R

class CustomCalendar : AppCompatActivity() {

    lateinit var backBtn : ImageView

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
        onPressBackListener()
    }

    fun initView(){
        backBtn = findViewById(R.id.imgBack)
    }

    fun onPressBackListener(){
        backBtn.setOnClickListener {
            finish()
        }
    }


}