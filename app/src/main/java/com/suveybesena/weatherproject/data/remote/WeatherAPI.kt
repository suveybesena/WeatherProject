package com.suveybesena.weatherproject.data.remote

import com.suveybesena.weatherproject.data.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather")
    fun location(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double, @Query("appid") appId: String
    ): Single<WeatherModel>

    @GET("data/2.5/weather")
    fun search(
        @Query("q") country: String,
        @Query("units") unit: String,
        @Query("appid") appId: String
    ): Single<WeatherModel>

}


