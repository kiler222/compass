package com.kiler.compassapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kiler.compassapp.data.AzimuthLiveData
import com.kiler.compassapp.data.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    private val azimuthData = AzimuthLiveData(application)

    fun getLocationData() = locationData

    fun getAzimuthData() = azimuthData

}