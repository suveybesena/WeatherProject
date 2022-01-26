package com.suveybesena.weatherproject.services.retrofit

import com.suveybesena.weatherproject.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIRetrofit {
    //https://api.openweathermap.org/data/2.5/weather?q=istanbul&appid=218696ef87ff7d83d67db8d46e55800f
    private val BASE_URL = "https://api.openweathermap.org/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)


    fun findCountry (country : String,unit:String,appId : String) : Single<WeatherModel>{
        return api.search(country,unit, appId)
    }
    fun findLocation( lat:Double, log:Double,appId: String):Single<WeatherModel>{
        return api.location(lat, log,appId)
    }



}