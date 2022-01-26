package com.suveybesena.weatherproject.presentation.notes

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.suveybesena.weatherproject.data.model.Weather
import com.suveybesena.weatherproject.presentation.base.BaseViewModel


class NoteViewModel (application: Application) : BaseViewModel(application) {

    val weatherDataInNotes = MutableLiveData<Weather>()


}