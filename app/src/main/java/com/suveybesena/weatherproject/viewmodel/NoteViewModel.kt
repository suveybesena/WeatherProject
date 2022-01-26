package com.suveybesena.weatherproject.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suveybesena.weatherproject.model.Weather


class NoteViewModel (application: Application) :BaseViewModel(application) {

    val weatherDataInNotes = MutableLiveData<Weather>()


}