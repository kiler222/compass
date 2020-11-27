package com.kiler.compassapp.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import com.kiler.compassapp.data.AzimuthLiveData
import com.kiler.compassapp.data.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)


    val sensorManager = getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val azimuthData = AzimuthLiveData(application)

    fun getLocationData() = locationData

    fun getAzimuthData() = azimuthData


}