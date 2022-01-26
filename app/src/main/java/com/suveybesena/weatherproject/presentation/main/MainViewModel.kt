package com.suveybesena.weatherproject.presentation.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.suveybesena.weatherproject.data.model.WeatherModel
import com.suveybesena.weatherproject.data.remote.WeatherAPIRetrofit
import com.suveybesena.weatherproject.presentation.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel(application) {

    val weatherData = MutableLiveData<WeatherModel>()
    val weatherError = MutableLiveData<Boolean>()
    val weatherProgress = MutableLiveData<Boolean>()
    private val weatherApiService = WeatherAPIRetrofit()
    private val disposable = CompositeDisposable()

    fun getDataFromSearch(country: String, unit: String, appId: String) {
        weatherProgress.value = true
        disposable.add(
            weatherApiService.findCountry(country, unit, appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {
                    override fun onSuccess(t: WeatherModel) {
                        weatherData.value = t
                        weatherError.value = false
                        weatherProgress.value = false

                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        weatherProgress.value = false
                    }

                })
        )
    }

    fun getDataFromLocation(lat: Double, log: Double, appId: String) {
        weatherProgress.value = true
        disposable.add(
            weatherApiService.findLocation(lat, log, appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {
                    override fun onSuccess(t: WeatherModel) {
                        weatherData.value = t
                        weatherError.value = false
                        weatherProgress.value = false

                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        weatherProgress.value = false
                    }

                })
        )
    }


}