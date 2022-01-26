package com.suveybesena.weatherproject.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suveybesena.weatherproject.R

class MainActivity : AppCompatActivity() {
    private lateinit var GET: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }


}