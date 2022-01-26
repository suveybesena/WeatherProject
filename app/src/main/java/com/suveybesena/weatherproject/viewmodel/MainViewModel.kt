package com.suveybesena.weatherproject.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suveybesena.weatherproject.model.Weather
import com.suveybesena.weatherproject.model.WeatherModel
import com.suveybesena.weatherproject.services.retrofit.WeatherAPIRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLongFieldUpdater

class MainViewModel (application: Application) :BaseViewModel(application) {

    val weatherData = MutableLiveData<WeatherModel>()
    val weather_Error = MutableLiveData<Boolean>()
    val weather_progress = MutableLiveData<Boolean>()

    private val weatherApiService = WeatherAPIRetrofit()
    private val disposable = CompositeDisposable()




     fun getDataFromSearch(country : String,unit:String,appId: String) {
        weather_progress.value = true

        disposable.add(
            weatherApiService.findCountry(country,unit, appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                        weatherData.value = t
                        weather_Error.value = false

                        weather_progress.value= false



                    }

                    override fun onError(e: Throwable) {
                        weather_Error.value = true
                        weather_progress.value= false
                    }

                })
        )
    }

    fun getDataFromLocation(lat:Double, log:Double,appId: String) {
        weather_progress.value = true

        disposable.add(
            weatherApiService.findLocation(lat,log,appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                        weatherData.value = t
                        weather_Error.value = false
                        weather_progress.value= false



                    }

                    override fun onError(e: Throwable) {
                        weather_Error.value = true
                        weather_progress.value= false
                    }

                })
        )
    }


}