package com.suveybesena.weatherproject.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.type.LatLng
import com.suveybesena.weatherproject.R
import com.suveybesena.weatherproject.utils.constans.APP_ID
import com.suveybesena.weatherproject.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import java.lang.Exception
import java.util.*


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    lateinit var fusedLocationClient :FusedLocationProviderClient



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


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        checkLocationPermission()


        getLiveData()
        search()

    }


    fun checkLocationPermission() {

       val task = fusedLocationClient.lastLocation
    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        return
    }
        task.addOnSuccessListener {

            println(it.latitude)
            viewModel.getDataFromLocation(it.latitude,it.longitude, APP_ID)
    }

    }


    private fun search() {

        imw_search_city_name.setOnClickListener {
            var countryName = edt_city_name.text.toString()
            viewModel.getDataFromSearch(countryName, "metric", APP_ID)
            getLiveData()
        }


    }


    private fun getLiveData() {
        viewModel.weatherData.observe(viewLifecycleOwner, Observer { data ->
            data?.let {


                tw_degree.text = "${data.main.temp.toInt().toString()}°C"
                tw_description.text = data.weather.get(0).main
                tw_city_name.text = data.name.toString()
                tw_humidity.text = "Humidity  ${data.main.humidity.toString()}"
                tw_speed.text = "Speed  %${data.wind.speed.toString()}"
                tw_mintemp.text = "Min  ${data.main.tempMin.toString() + "°C"}"
                tw_maxTemp.text = "Max  ${data.main.tempMax.toString() + "°C"}"
                var weather = data.weather.get(0).main
                var cloudly = R.drawable.cloudly
                var sunny = R.drawable.sunny
                var snowy = R.drawable.snowy
                var foggy = R.drawable.foggy

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

        viewModel.weather_progress.observe(viewLifecycleOwner, Observer { load ->
            load?.let {
                if (it) {
                    twErrorMessage.visibility = View.GONE
                    pb_loading.visibility = View.VISIBLE
                } else {
                    pb_loading.visibility = View.GONE
                }

            }


        })

        viewModel.weather_Error.observe(viewLifecycleOwner, Observer { error ->
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


