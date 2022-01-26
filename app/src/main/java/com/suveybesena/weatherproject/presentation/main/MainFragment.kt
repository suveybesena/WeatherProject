package com.suveybesena.weatherproject.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.suveybesena.weatherproject.R
import com.suveybesena.weatherproject.utils.Constans.APP_ID
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocation()
        getLocation()
        getLiveData()
        search()

    }

    private fun fusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun getLocation() {
        val task = fusedLocationClient.lastLocation
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        task.addOnSuccessListener {
            viewModel.getDataFromLocation(it.latitude, it.longitude, APP_ID)
        }
    }

    private fun search() {
        imw_search_city_name.setOnClickListener {
            val countryName = edt_city_name.text.toString()
            viewModel.getDataFromSearch(countryName, "metric", APP_ID)
            getLiveData()
        }

    }

    private fun getLiveData() {
        viewModel.weatherData.observe(viewLifecycleOwner, Observer { data ->
            data?.let {

                tw_degree.text = "${data.main.temp.toInt()}°C"
                tw_description.text = data.weather.get(0).main
                tw_city_name.text = data.name
                tw_humidity.text = "Humidity  ${data.main.humidity}"
                tw_speed.text = "Speed  %${data.wind.speed}"
                tw_mintemp.text = "Min  ${data.main.tempMin.toInt()} °C"
                tw_maxTemp.text = "Max  ${data.main.tempMax.toInt()} °C"
                val weather = data.weather.get(0).main
                val cloudly = R.drawable.cloudly
                val sunny = R.drawable.sunny
                val snowy = R.drawable.snowy
                val foggy = R.drawable.foggy

                swipeRefreshLayout.background
                if (weather.contains("Cloud")) {
                    main_background.setImageResource(cloudly)
                } else if (weather.contains("Sun")) {
                    main_background.setImageResource(sunny)
                } else if (weather.contains("Snow")) {
                    main_background.setImageResource(snowy)
                } else if (weather.contains("Mist")) {
                    main_background.setImageResource(foggy)
                } else {
                    main_background.setImageResource(foggy)
                }

                //http://openweathermap.org/img/wn/10d@2x.png
                Glide.with(this)
                    .load("http://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(img_weather_icon)
                btw_notes.setOnClickListener {
                    val action = MainFragmentDirections.actionMainFragmentToNotesFragment()
                    Navigation.findNavController(it).navigate(action)
                }


            }

        })

        viewModel.weatherProgress.observe(viewLifecycleOwner, Observer { load ->
            load?.let {
                if (it) {
                    twErrorMessage.visibility = View.GONE
                    pb_loading.visibility = View.VISIBLE
                } else {
                    pb_loading.visibility = View.GONE
                }

            }


        })

        viewModel.weatherError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    twErrorMessage.visibility = View.VISIBLE
                    pb_loading.visibility = View.GONE
                } else {
                    twErrorMessage.visibility = View.GONE
                }

            }


        })
    }

}


