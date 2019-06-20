package com.example.chatnewsfeed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonFaculty.setOnClickListener {
            val intent = Intent(this, Activity_faculty::class.java)
            startActivity(intent)
        }
        buttonStudent.setOnClickListener {
            val intent = Intent(this, Activity_student::class.java)
            startActivity(intent)
        }
    }
}
