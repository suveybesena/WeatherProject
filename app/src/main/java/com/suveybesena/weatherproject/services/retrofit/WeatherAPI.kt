package com.suveybesena.weatherproject.services.retrofit

import com.suveybesena.weatherproject.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.function.DoubleUnaryOperator

//https://api.openweathermap.org/data/2.5/weather?q=istanbul&appid=218696ef87ff7d83d67db8d46e55800f
interface WeatherAPI {


    @GET("data/2.5/weather")
    fun location(@Query("lat") lat:Double,
                 @Query("lon") lon:Double,@Query("appid") appId: String ) :Single<WeatherModel>



    //@GET("data/2.5/weather?q&appid=218696ef87ff7d83d67db8d46e55800f")
    //fun search(@Query("q") country: String): Single<WeatherModel>

    @GET("data/2.5/weather")
    fun search(@Query("q") country: String, @Query ("units" ) unit:String, @Query("appid" ) appId: String) : Single<WeatherModel>

}


