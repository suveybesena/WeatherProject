package com.suveybesena.weatherproject.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(

    val description: String,
    val icon: String,
    val id: Int,

    val main: String
){
    @PrimaryKey(autoGenerate = true)
    var uuid :Int = 0
}