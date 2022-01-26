package com.suveybesena.weatherproject.data.model


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Clouds(
    @ColumnInfo(name= "all")
    @SerializedName("all")
    val all: Int
)